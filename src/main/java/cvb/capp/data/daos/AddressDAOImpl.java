package cvb.capp.data.daos;

import cvb.capp.business.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class AddressDAOImpl implements AddressDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int addAddress(Address address) {
        String streetName = address.getStreetName();
        int streetNo = address.getStreetNo();
        int apartment = address.getApartment();
        String locality = address.getLocality();
        int zipcode = address.getZipcode();
        String sql = "INSERT INTO Addresses (StreetName, StreetNo, Apartment, Locality, Zipcode)" +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, streetName);
            ps.setString(2, String.valueOf(streetNo));
            ps.setString(3, String.valueOf(apartment));
            ps.setString(4, locality);
            ps.setString(5, String.valueOf(zipcode));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public boolean updateAddress(Address address, int address_id) {
        String streetName = address.getStreetName();
        int streetNo = address.getStreetNo();
        int apartment = address.getApartment();
        String locality = address.getLocality();
        int zipcode = address.getZipcode();
        String sql = "UPDATE Addresses SET StreetName = ?, StreetNo = ?, Apartment = ?, " +
                "Locality = ?, Zipcode = ? WHERE id = ?";
        return jdbcTemplate.update(sql, streetName, streetNo, apartment, locality, zipcode, address_id) >= 0;
    }

    @Override
    public Address findAddressById(int addressId) {
        String sql = "SELECT * FROM Addresses WHERE id = ?";
        RowMapper<Address> rowMapper = new BeanPropertyRowMapper<>(Address.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, addressId);
    }
}
