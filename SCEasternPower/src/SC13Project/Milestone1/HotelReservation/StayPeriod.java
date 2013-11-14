package SC13Project.Milestone1.HotelReservation;

//Please do not change the name of the package or this class
public class StayPeriod {
	/**
	 * date of checkin
	 */
    private ReservationDate checkin; 
    /**
     * date of checkout
     */
    private ReservationDate checkout;
	
    public ReservationDate getCheckin() {
		return checkin;
	}
	
    public void setCheckin(ReservationDate checkin) {
		this.checkin = checkin;
	}

	public ReservationDate getCheckout() {
		return checkout;
	}

	public void setCheckout(ReservationDate checkout) {
		this.checkout = checkout;
	} 
}
