package SC13Project.Milestone1.Warehouse;

import java.util.List;

//Please do not change the name of the package or the interface
public interface WarehouseWS {

	/** 
	 * Query the amount of available items in the warehouse by resourceID
	 * @param resourceID the resourceID of the item in the warehouse
	 * @return the amount of the item in the warehouse
	 * 
	 * 
	 */
	public int query(String resourceID);
	
	/**
	 * Remove the amount of items from the warehouse
	 * @param resourceID the resourceID of the item in the warehouse
	 * @param amount the amount of items need to be picked up
	 * @return true if the operation is successfully; otherwise false
	 * @exception NotEnoughItemException
	 * 
	 * 
	 */
	public boolean pickupItems(String resourceID, int amount) throws NotEnoughItemException;
	
	/**
	 * Complement the amount of items to the warehouse
	 * @param resourceID  the resourceID of the item in the warehouse
	 * @param amount the amount of items need to complement
	 * @return the new amount of items in the warehouse
	 * 
	 * 
	 */
	public int complementStock(String resourceID, int amount); 
	
	/**
	 * Hold some items for later use. The holding items are invisible to the query operation.
	 * The items can only be accessed by the ID of the holding request.
	 * @param resourceID the resourceID of the item in the warehouse
	 * @param amount the amount of items need to be held
	 * @return the ID of the holding request (the ID can be used to pickup items or cancel the holding)
	 * 
	 *  
	 */
	public String holdItems(String resourceID, int amount) throws NotEnoughItemException;
	
	
	/**
	 * Cancel the holding request. The holding items are released.
	 * @param holdingID the ID of the holding request
	 * 
	 * 
	 */
	public void cancelHoldingItems(String holdingID);
	
	/**
	 * Pick up the holding items. Remove holding items from the warehouse
	 * @param holdingID the ID of the holding request
	 * @return true if the operation is successful; otherwise false
	 * @exception the holdingID is invalid
	 * 
	 * 
	 */
	public boolean pickupHoldingItems(String holdingID) throws InvalidHoldingIDException;
	
}

