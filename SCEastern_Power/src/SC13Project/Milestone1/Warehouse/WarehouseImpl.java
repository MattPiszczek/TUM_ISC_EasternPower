package SC13Project.Milestone1.Warehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import SC13Project.Milestone1.Warehouse.Database.ObjectFactory;
import SC13Project.Milestone1.Warehouse.Database.WareHouse;

//Please do not change the name of the package or this interface
//Please add here your implementation
public class WarehouseImpl implements WarehouseWS {

	private String packageName;
	private JAXBContext warehouseContext;
	private Unmarshaller wareHouseUnm;
	private Marshaller wareHouseMar;
	private File databaseFile;

	// public void setDatabaseFile(File databaseFile) {
	// this.databaseFile = databaseFile;
	// }
	//
	// public void setDatabaseFile(String fileLocation) {
	// this.databaseFile = new File(fileLocation);
	// }

	public WarehouseImpl() {
		packageName = WareHouse.class.getPackage().getName();
		ClassLoader cl = SC13Project.Milestone1.Warehouse.Database.ObjectFactory.class
				.getClassLoader();
		try {
			warehouseContext = JAXBContext.newInstance(packageName, cl);
			wareHouseUnm = warehouseContext.createUnmarshaller();
			wareHouseMar = warehouseContext.createMarshaller();
		} catch (JAXBException jaxbE) {
			jaxbE.printStackTrace();
			System.exit(1);
		}

		databaseFile = new File("../datasource/ds_29_2.xml");
	}

	@Override
	public int query(String resourceID) {

		// unmarshaling
		WareHouse wh = this.unmarshaledObjects();

		// true action
		return wh.query(resourceID);
	}

	@Override
	public boolean pickupItems(String resourceID, int amount)
			throws NotEnoughItemException {

		boolean result = false;

		// unmarshaling
		WareHouse wh = null;
		wh = this.unmarshaledObjects();

		// true action
		result = wh.pickupItems(resourceID, amount);

		// marshaling state after changes
		this.marshalDatabse(wh);

		return result;
	}

	@Override
	public int complementStock(String resourceID, int amount) {

		int result;

		// unmarshaling
		WareHouse wh = null;
		wh = wh = this.unmarshaledObjects();

		// true action
		result = wh.complementStock(resourceID, amount);

		// marshaling state after changes
		this.marshalDatabse(wh);

		return result;
	}

	@Override
	public String holdItems(String resourceID, int amount)
			throws NotEnoughItemException {

		String holdingReqID;

		// unmarshaling
		WareHouse wh = null;
		wh = this.unmarshaledObjects();

		// true action
		holdingReqID = wh.holdItems(resourceID, amount);

		// marshaling state after changes
		this.marshalDatabse(wh);

		return holdingReqID;
	}

	@Override
	public void cancelHoldingItems(String holdingID) {

		// unmarshaling
		WareHouse wh = null;
		wh = this.unmarshaledObjects();

		// true action
		wh.cancelHoldingItems(holdingID);

		// marshaling state after changes
		this.marshalDatabse(wh);
	}

	@Override
	public boolean pickupHoldingItems(String holdingID)
			throws InvalidHoldingIDException {

		boolean result;

		// unmarshaling
		WareHouse wh = null;
		wh = this.unmarshaledObjects();

		// true action
		result = wh.pickupHoldingItems(holdingID);

		// marshaling state after changes
		this.marshalDatabse(wh);

		return result;
	}

	private WareHouse unmarshaledObjects() {

		// TODO: find out if exception is need to be thrown and catched in
		// higher level

		WareHouse wh = null;
		JAXBElement<WareHouse> rootElement;

		try {
			rootElement = (JAXBElement<WareHouse>) wareHouseUnm.unmarshal(new FileInputStream(databaseFile));
			wh = rootElement.getValue();
		} catch (Exception e) {
			System.err
					.println("Unsuccessful unmarshaling Ware House object from XML file.");
			e.printStackTrace();
		}

		return wh;

	}

	private void marshalDatabse(WareHouse wh) {
		try {
			wareHouseMar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (PropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ObjectFactory jaxbWarehouseFactory = new ObjectFactory();
		JAXBElement<WareHouse> jaxbWarehouse = jaxbWarehouseFactory.createWarehouse(wh);
		
		try {
			this.wareHouseMar.marshal(jaxbWarehouse, new FileOutputStream(databaseFile));
		} catch (Exception e) {
			// TODO examine if where this exception should be hanled
			System.err
					.println("Error while marshaling changes to XML database file.");
			e.printStackTrace();
		}
	}
}
