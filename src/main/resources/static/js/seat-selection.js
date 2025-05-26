document.addEventListener('DOMContentLoaded', function() {
    const areaSelect = document.getElementById('areaSelect');
    const seatMap = document.getElementById('seatMap');
    const nextButton = document.getElementById('nextButton');
    let selectedSeat = null;
    const campaignId = 1; // This should be passed from the backend or URL parameter
    const MAX_SEATS_PER_ROW = 10;

    // Generate seat map
    function generateSeatMap(seats) {
        seatMap.innerHTML = '';
        
        // Group seats by row
        const seatsByRow = {};
        seats.forEach(seat => {
            if (!seatsByRow[seat.seat_row]) {
                seatsByRow[seat.seat_row] = [];
            }
            seatsByRow[seat.seat_row].push(seat);
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
                    seatElement.dataset.row = seat.seat_row;
                    seatElement.dataset.column = seat.seat_column;
                    seatElement.textContent = `${seat.seat_row}-${seat.seat_column}`;
                    
                    if (seat.status === 'absent') {
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
            case 'absent':
                return 'available';
            case 'occupied':
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
        
        // Here you would typically send the data to your backend
        console.log('Selected seat:', seatData);
        // Redirect to order creation page or show order form
        window.location.href = `/order/create?seatId=${seatData.seatId}`;
    });
}); 