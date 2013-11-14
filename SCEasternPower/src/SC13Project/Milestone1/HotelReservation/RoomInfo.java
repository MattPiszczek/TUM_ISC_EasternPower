package SC13Project.Milestone1.HotelReservation;

//Please do not change the name of the package or this class
public class RoomInfo {

	/**
	 * Room type
	 */
	private String type; 
	/**
	 * number of vacancies
	 */
	private int vacancies;
	/**
	 * rate of the hotel
	 */
	private int rate; 
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public int getVacancies() {
		return vacancies;
	}

	public void setVacancies(int vacancies) {
		this.vacancies = vacancies;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
	
}
