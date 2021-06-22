package cvb.capp.data.daos;

import cvb.capp.business.models.Address;

public interface AddressDAO {
    int addAddress(Address address);

    boolean updateAddress(Address address, int address_id);

    Address findAddressById(int addressId);
}
