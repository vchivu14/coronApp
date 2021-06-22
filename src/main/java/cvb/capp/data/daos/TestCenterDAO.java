package cvb.capp.data.daos;

import cvb.capp.business.models.TestCenter;

import java.util.List;

public interface TestCenterDAO {
    void addTestCenter(TestCenter testCenter, int addressId);

    boolean removeTestCenter(int testCenterId);

    boolean updateTestCenter(TestCenter testCenter, int testCenterId);

    List<TestCenter> fetchAllTestCenters();

    TestCenter findTestCenterById(int testCenterId);

    TestCenter findTestCenterByName(String name);
}
