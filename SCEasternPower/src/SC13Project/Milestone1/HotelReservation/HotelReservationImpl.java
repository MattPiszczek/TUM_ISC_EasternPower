package SC13Project.Milestone1.HotelReservation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import SC13Project.Milestone1.HotelReservation.Database.BookingInfo;
import SC13Project.Milestone1.HotelReservation.Database.BookingList;
import SC13Project.Milestone1.HotelReservation.Database.HotelInfo;
import SC13Project.Milestone1.HotelReservation.Database.ObjectFactory;
import SC13Project.Milestone1.HotelReservation.Database.ReservationDateInfo;
import SC13Project.Milestone1.HotelReservation.Database.RoomList;
import SC13Project.Milestone1.HotelReservation.Database.StayPeriodType;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Please do not change the name of the package or this interface
//Please add here your implementation
public class HotelReservationImpl implements HotelReservationWS{
	
	/**
	 * Unmarshall Hotel
	 * @return the hotel node
	 * @exception FileNotFoundException
	 * @exception JAXBException
	 * 
	 * 
	 */
	private HotelInfo UnmarshallHotel(){
		//unmarshall data from xml sample database
		JAXBContext jc;
		Unmarshaller u;
		JAXBElement<HotelInfo> root;
		String packageName = HotelInfo.class.getPackage().getName();
		try {
			jc = JAXBContext.newInstance(packageName);
			 u = jc.createUnmarshaller();
			try {
				root = (JAXBElement<HotelInfo>)u.unmarshal(new FileInputStream("ds_29_4.xml"));
			} catch (FileNotFoundException e) {
				root = null;
				e.printStackTrace();
				System.exit(1);
			}
		} catch (JAXBException e) {
			root = null;
			e.printStackTrace();
			System.exit(1);
		}
		
	    return root.getValue(); 
	}
	/**
	 * Marshall Hotel
	 * @param the hotel node
	 * @exception FileNotFoundException
	 * @exception JAXBException
	 * 
	 * 
	 */
	private void MarshallHotel(HotelInfo hotel){
		JAXBContext jc;
		Marshaller m;
		JAXBElement<HotelInfo> output;
		String packageName = HotelInfo.class.getPackage().getName();
		try{
		    jc = JAXBContext.newInstance(packageName);
		    m = jc.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		    ObjectFactory obf = new ObjectFactory();
		    output = obf.createHotel(hotel);
		} catch(JAXBException e) {
			output = null;
			m = null;
			e.printStackTrace();
			System.exit(1);
		}
	    
	    try {
			m.marshal(output, new FileOutputStream("ds_29_4.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);

		} catch (JAXBException e) {
			e.printStackTrace();
			System.exit(1);

		}
	}
	@Override
	public List<RoomInfo> getAvailableRooms(StayPeriod period) {
		 HotelInfo hotel = UnmarshallHotel();
	     RoomList roomList = hotel.getRooms();
	     List<SC13Project.Milestone1.HotelReservation.Database.RoomInfo> rooms = roomList.getRoom();
	     BookingList bookingList = hotel.getBookings();
	     List<BookingInfo> bookings = bookingList.getBooking();
	     
	     // finding the room
	     // creating a array list of available rooms
 		 List<RoomInfo> availableRooms = new ArrayList<RoomInfo>();
 		 //at the beginning all of rooms are free = there are no bookings
 		 // copy pasteur
 		 for(SC13Project.Milestone1.HotelReservation.Database.RoomInfo room : rooms)
 		 {
 			 RoomInfo roomToAdd = new RoomInfo();
 			 roomToAdd.setVacancies(room.getTotalAmount());
 			 roomToAdd.setRate(room.getRate());
 			 roomToAdd.setType(room.getType());
 			 availableRooms.add(roomToAdd);
 		 }
 		
 		for(BookingInfo booking : bookings) {
 			for(SC13Project.Milestone1.HotelReservation.Database.RoomInfo room : rooms) {
	    		 
	    		 // if there is a booking for a specific room 
	        	boolean isColliding = false; 
	    		 if(room.getType().equalsIgnoreCase(booking.getType()))
	    		 {
	    			 //checking if the staying periods collide - comparing dates
	    			 StayPeriodType bookingPeriod = booking.getStayPeriod();
	    			 
	    			 // check in/out of booking
	    			ReservationDateInfo bookingCheckIn = bookingPeriod.getCheckin();
	    			ReservationDateInfo bookingCheckOut = bookingPeriod.getCheckout();
    	    		
	    			//check in/out of parameter
	    			ReservationDate demandedCheckIn = period.getCheckin();
	    			ReservationDate demandedCheckOut = period.getCheckout();
	    			
	    			// converting date, month and year to date
	    			try{
		    			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
	    	        	Date bookingCheckInDate = sdf.parse( bookingCheckIn.getDate() + "/" + bookingCheckIn.getMonth() + "/" + bookingCheckIn.getYear());
	    	        	Date bookingCheckOutDate = sdf.parse( bookingCheckIn.getDate() + "/" + bookingCheckIn.getMonth() + "/" + bookingCheckIn.getYear());
	    	        	Date demandedCheckInDate = sdf.parse(demandedCheckIn.getDate() + "/" + demandedCheckIn.getMonth() + "/" + demandedCheckIn.getYear());
	    	        	Date demandedCheckOutDate = sdf.parse(demandedCheckOut.getDate() + "/" + demandedCheckOut.getMonth() + "/" + demandedCheckOut.getYear());
	    	        	
	    	        	// comparing two dates
	    	        	if(demandedCheckInDate.compareTo(bookingCheckInDate)>=0 && demandedCheckInDate.compareTo(bookingCheckOutDate) <= 0){
	    	        		// daty sie pokrywaja	
	    	        		isColliding = true;
	    	        	}
		        		else if(demandedCheckInDate.compareTo(bookingCheckInDate)<0 && demandedCheckOutDate.compareTo(bookingCheckInDate)>=0 && demandedCheckOutDate.compareTo(bookingCheckOutDate)<0){
		        			{
		        				// daty sie pokrywaja
		        				isColliding = true;
		        			}
	    	        	}else {
	    	        	// daty sie nie pokrywaja
	    	        		isColliding = false;   	
	   	    			 }
	    			}catch(ParseException ex){
	    	    		ex.printStackTrace();
	    	    	}	 
	    		 }
	    		 if(isColliding == true){
	    			 // pomniejszam dany typ pokoju z listy wolnych o ilosc amount w zamowieniu
	    			 for(RoomInfo ri : availableRooms){
	    				 if(ri.getType().equalsIgnoreCase(booking.getType()))
	    					 //typ sie pokrywa
	    					 ri.setVacancies((ri.getVacancies()-booking.getAmount()));
	    			 }
	    		 }
	    		  
	    	 }
	     }
		return availableRooms; 
	}
	@Override
	public String bookRoom(String type, int amount, StayPeriod period) throws UnAvailableException{
	String bookingID = null;
	// creating a array list of available rooms
	 List<RoomInfo> availableRooms = getAvailableRooms(period);
	 // if there is no rooms available at all
	 if(availableRooms.isEmpty() == true)
		 throw new UnAvailableException();
	 // searching for the proper room to book
	 for(RoomInfo roomToBook : availableRooms ){
		 //check if types are matched
		 if(roomToBook.getType().equalsIgnoreCase(type))
		 {
			 // check if there are enough vacancies
			 if(roomToBook.getVacancies()>=amount){
				 //all good sir, book the room!
				//unmarshall data from xml sample database
			     HotelInfo hotel = UnmarshallHotel(); 
			     BookingList bookingList = hotel.getBookings();
			     List<BookingInfo> bookings = bookingList.getBooking();
			     BookingInfo newBooking = new BookingInfo();
			     StayPeriodType spt = new StayPeriodType();
			     ReservationDateInfo sptCheckIn = new ReservationDateInfo();
			     sptCheckIn.setDate(period.getCheckin().getDate());
			     sptCheckIn.setMonth(period.getCheckin().getMonth());
			     sptCheckIn.setYear(period.getCheckin().getYear());
			     ReservationDateInfo sptCheckOut = new ReservationDateInfo();
			     sptCheckOut.setDate(period.getCheckout().getDate());
			     sptCheckOut.setMonth(period.getCheckout().getMonth());
			     sptCheckOut.setYear(period.getCheckout().getYear());
			     spt.setCheckin(sptCheckIn);
			     spt.setCheckout(sptCheckOut);

			     //create new periodType
			     newBooking.setStayPeriod(spt);
			     newBooking.setType(type);
			     newBooking.setAmount(amount);
			     Integer i = new Integer(0);
			     //find booking id
			     if(bookings.isEmpty() == true)
			     {
			    	 newBooking.setBookingID(i.toString());
			     	 bookingID = i.toString();
			     }
			     else{
			    	 BookingInfo lastBooking = bookings.get(bookings.size()-1);
			    	 Integer index = new Integer(Integer.parseInt(lastBooking.getBookingID()+1));
			    	 newBooking.setBookingID(index.toString());
			    	 bookingID = index.toString();
			     }
			     bookings.add(newBooking);
			     //marshall data to xml
			     MarshallHotel(hotel); 
			     
			     break;
			 }
			 else{
				 throw new UnAvailableException();
			 }
		 }
		 else{
			 throw new UnAvailableException();
		 }
	 }
	 return bookingID;
	}
	@Override
	public void cancelBooking(String bookingID) {
		//unmarshall data from xml sample database
	     HotelInfo hotel = UnmarshallHotel(); 
	     BookingList bookingList = hotel.getBookings();
	     List<BookingInfo> bookings = bookingList.getBooking();
	     
	     //erase the specific booking
    	 for(BookingInfo booking : bookings){
    		 //check if id matches
    		 if(booking.getBookingID().equalsIgnoreCase(bookingID)){
    			 bookings.remove(booking);
    			 break;
    		 }
    	 }
	     
	}

}

