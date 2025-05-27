document.addEventListener('DOMContentLoaded', function() {
    const areaSelect = document.getElementById('areaSelect');
    const seatMap = document.getElementById('seatMap');
    const nextButton = document.getElementById('nextButton');
    let selectedSeat = null;
    const campaignId = 1; // This should be passed from the backend or URL parameter
    const MAX_SEATS_PER_ROW = 10;
    const userId = 1; // This should be passed from the backend or URL parameter

    // Generate seat map
    function generateSeatMap(seats) {
        seatMap.innerHTML = '';
        
        // Group seats by row
        const seatsByRow = {};
        seats.forEach(seat => {
            if (!seatsByRow[seat.seatRow]) {
                seatsByRow[seat.seatRow] = [];
            }
            seatsByRow[seat.seatRow].push(seat);
        });

        // Sort rows and create seat map
        Object.keys(seatsByRow).sort((a, b) => a - b).forEach(row => {
            const rowDiv = document.createElement('div');
            rowDiv.className = 'mb-2';
            
            // Sort seats by column and limit to MAX_SEATS_PER_ROW
            const rowSeats = seatsByRow[row].sort((a, b) => a.seat_column - b.seat_column);
            
            // Create multiple rows if needed
            for (let i = 0; i < rowSeats.length; i += MAX_SEATS_PER_ROW) {
                const rowSegment = document.createElement('div');
                rowSegment.className = 'mb-2';
                
                const segmentSeats = rowSeats.slice(i, i + MAX_SEATS_PER_ROW);
                segmentSeats.forEach(seat => {
                    const seatElement = document.createElement('div');
                    seatElement.className = `seat ${getSeatStatusClass(seat.status)}`;
                    seatElement.dataset.id = seat.id;
                    seatElement.dataset.row = seat.seatRow;
                    seatElement.dataset.column = seat.seatColumn;
                    seatElement.textContent = `${seat.seatRow}-${seat.seatColumn}`;
                    
                    if (seat.status === 'available') {
                        seatElement.addEventListener('click', function() {
                            if (this.classList.contains('unavailable')) return;
                            
                            // Deselect previous seat
                            if (selectedSeat) {
                                selectedSeat.classList.remove('selected');
                                selectedSeat.classList.add('available');
                            }
                            
                            // Select new seat
                            this.classList.remove('available');
                            this.classList.add('selected');
                            selectedSeat = this;
                            
                            // Enable next button
                            nextButton.disabled = false;
                        });
                    }
                    
                    rowSegment.appendChild(seatElement);
                });
                rowDiv.appendChild(rowSegment);
            }
            seatMap.appendChild(rowDiv);
        });
    }

    function getSeatStatusClass(status) {
        switch (status) {
            case 'available':
                return 'available';
            case 'reserved':
            case 'purchased':
                return 'unavailable';
            default:
                return 'available';
        }
    }

    // Handle area selection
    areaSelect.addEventListener('change', function() {
        if (this.value) {
            // Fetch seats from backend
            fetch(`/api/seats/${campaignId}/${this.value}`)
                .then(response => response.json())
                .then(seats => {
                    generateSeatMap(seats);
                })
                .catch(error => {
                    console.error('Error fetching seats:', error);
                    seatMap.innerHTML = '<div class="alert alert-danger">Error loading seats</div>';
                });
        } else {
            seatMap.innerHTML = '';
            nextButton.disabled = true;
        }
    });

    // Handle next button click
    nextButton.addEventListener('click', function() {
        if (!selectedSeat) return;
        
        const seatData = {
            area: areaSelect.value,
            row: selectedSeat.dataset.row,
            column: selectedSeat.dataset.column,
            seatId: selectedSeat.dataset.id
        };

        buyTicket(seatData);
    });
});

async function buyTicket(seatData) {
    const nextButton = document.getElementById('nextButton');
    nextButton.disabled = true;
    nextButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Processing...';

    try {
        // Initial purchase request
        const response = await fetch('/oper/buyTicket', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: 1, // TODO: Get from session
                campaignId: 1, // TODO: Get from URL or session
                area: seatData.area,
                row: seatData.row,
                column: seatData.column
            })
        });

        const data = await response.json();

        if (response.ok) {
            if (data.error === 'SEAT_ALREADY_RESERVED') {
                window.location.href = '/ticket/sorry';
                return;
            }

            // Start polling for status if we have a requestId
            if (data.requestId) {
                await pollPurchaseStatus(data.requestId, seatData);
            }
        } else {
            handleError(data);
        }
    } catch (error) {
        console.error('Error:', error);
        handleError({ message: 'An error occurred. Please try again.' });
    }
}

async function pollPurchaseStatus(requestId, seatData) {
    const maxAttempts = 60; // 1 minute with 1-second intervals
    let attempts = 0;

    const poll = async () => {
        try {
            const response = await fetch(`/oper/getPurchaseStatus?requestId=${requestId}`);
            const data = await response.json();

            if (response.ok) {
                switch (data.status) {
                    case 'COMPLETED':
                        if (data.error === 'SEAT_ALREADY_RESERVED') {
                            window.location.href = '/ticket/sorry';
                        } else {
                            // Redirect to payment page with ticket information
                            const paymentData = {
                                ticketId: data.ticketId,
                                seatInfo: `Area ${seatData.area}, Row ${seatData.row}, Column ${seatData.column}`,
                                price: data.price
                            };
                            window.location.href = `/ticket/payment?${new URLSearchParams(paymentData).toString()}`;
//                            const seatInfo = `Area ${seatData.area}, Row ${seatData.row}, Column ${seatData.column}`;
//                            window.location.href = `/ticket/success?ticketId=${data.ticketId}&seatInfo=${encodeURIComponent(seatInfo)}`;
                        }
                        return;
                    case 'FAILED':
                        handleError({ message: data.message || 'Purchase failed. Please try again.' });
                        return;
                    case 'PROCESSING':
                        updateLoadingMessage('Processing your purchase...');
                        break;
                    case 'PENDING':
                        updateLoadingMessage('Your request is in queue...');
                        break;
                }
            } else {
                handleError(data);
                return;
            }

            attempts++;
            if (attempts < maxAttempts) {
                setTimeout(poll, 1000);
            } else {
                handleError({ message: 'Purchase request timed out. Please check your ticket status later.' });
            }
        } catch (error) {
            console.error('Error polling status:', error);
            handleError({ message: 'Error checking purchase status. Please try again.' });
        }
    };

    // Start polling
    await poll();
}

function updateLoadingMessage(message) {
    const nextButton = document.getElementById('nextButton');
    nextButton.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ${message}`;
}

function handleError(data) {
    const nextButton = document.getElementById('nextButton');
    nextButton.disabled = false;
    nextButton.textContent = 'Next';

    if (data.error === 'SEAT_ALREADY_RESERVED') {
        window.location.href = '/ticket/sorry';
    } else {
        const alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-danger mt-3';
        alertDiv.textContent = data.message || 'Failed to purchase ticket. Please try again.';
        document.querySelector('.container').appendChild(alertDiv);
    }
}
