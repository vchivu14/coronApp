package cvb.capp.presentation;

import cvb.capp.business.models.Address;
import cvb.capp.business.models.TestCenter;
import cvb.capp.business.services.AdminService;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.PersonDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import cvb.capp.business.services.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @ModelAttribute("testCenterDTO")
    public TestCenterDTO testCenterDTO() {
        return new TestCenterDTO();
    }

    @ModelAttribute("address")
    public Address address() {
        return new Address();
    }

    @ModelAttribute("addressDTO")
    public AddressDTO addressDTO() {
        return new AddressDTO();
    }

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username,
                             Model model) {
        model.addAttribute(username);
        return "adminPage";
    }

    @GetMapping("/testCenters")
    public String showTestCenters(@ModelAttribute TestCenterDTO testCenterDTO, Model model) {
        model.addAttribute(adminService.fetchAllTestCenters());
        return "/adminTestCenters";
    }

    @GetMapping("/testCenters/{idTestCenter}")
    public String showTestCenter(@PathVariable("idTestCenter") int idTestCenter,
                                 @ModelAttribute TestCenterDTO testCenterDTO,
                                 @ModelAttribute AddressDTO addressDTO, Model model) {
        model.addAttribute(adminService.findTestCenterById(idTestCenter));
        model.addAttribute(adminService.getAddressForTestCenter(idTestCenter));
        return "/adminTestCenterView";
    }

    @GetMapping("/testCenters/{idTestCenter}/delete")
    public String removeTestCenter(@PathVariable("idTestCenter") int idTestCenter) {
        boolean delete = adminService.removeTestCenter(idTestCenter);
        if (delete) {
            return "redirect:/admin/testCenters";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/testCenters/update/{idTestCenter}")
    public String updateTestCenterForm(@PathVariable("idTestCenter") int idTestCenter,
                                       @ModelAttribute TestCenterDTO testCenterDTO, Model model) {
        model.addAttribute(testCenterDTO);
        return "adminTestCenterUpdateForm";
    }

    @PostMapping("/testCenters/update/{idTestCenter}")
    public String updateTestCenter(@PathVariable("idTestCenter") int idTestCenter,
                                   @ModelAttribute TestCenterDTO testCenterDTO) {
        boolean update = adminService.updateTestCenter(testCenterDTO, idTestCenter);
        if (update) {
            return "redirect:/admin/testCenters/"+idTestCenter;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/testCenters/address/update/{idTestCenter}")
    public String updateTestCenterAddressForm(@PathVariable("idTestCenter") int idTestCenter,
                                              @ModelAttribute AddressDTO addressDTO,
                                              Model model) {
        model.addAttribute(adminService.findTestCenterById(idTestCenter));
        model.addAttribute(adminService.getAddressForTestCenter(idTestCenter));
        return "adminTestCenterAddressUpdateForm";
    }

    @PostMapping("/testCenters/address/update/{idTestCenter}")
    public String updateTestCenterAddress(@PathVariable("idTestCenter") int idTestCenter,
                                          @ModelAttribute AddressDTO addressDTO) {
        boolean update = adminService.updateAddressForTestCenter(addressDTO, idTestCenter);
        if (update) {
            return "redirect:/admin/testCenters/"+idTestCenter;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/add/testCenterAddress")
    public String registerTestCenterAddress(@ModelAttribute Address address, Model model) {
        model.addAttribute(address);
        return "/adminRegisterTestCenterAddress";
    }

    @PostMapping("/add/testCenterAddress")
    public String addTestCenterAddress(@ModelAttribute Address address) {
        int addressId = adminService.addAddress(address);
        return "redirect:/admin/add/testCenter/" + addressId;
    }

    @GetMapping("/add/testCenter/{idA}")
    public String registerTestCenter(@PathVariable("idA") int idA,
                                     @ModelAttribute TestCenterDTO testCenterDTO, Model model) {
        model.addAttribute(testCenterDTO);
        model.addAttribute(idA);
        return "/adminRegisterTestCenter";
    }

    @PostMapping("/add/testCenter/{idA}")
    public String addTestCenter(@PathVariable("idA") int idA, @ModelAttribute TestCenterDTO testCenterDTO) {
        adminService.addTestCenter(testCenterDTO, idA);
        return "redirect:/admin/testCenters";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute(adminService.fetchAllUsers());
        return "/adminUsers";
    }

    @GetMapping("/user/{userId}")
    public String showUser(@PathVariable("userId") int userId,
                           @ModelAttribute UserDTO userDTO,
                           Model model) {
        model.addAttribute(userDTO);
        model.addAttribute(userId);
        model.addAttribute(adminService.findUserByUserId(userId));
        return "adminUserView";
    }

    @GetMapping("/user/delete/{userId}")
    public String removeUser(@PathVariable("userId") int userId) {
        boolean delete = adminService.removeUser(userId);
        if (delete) {
            return "redirect:/admin/users";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/user/update/{userId}")
    public String updateUserForm(@PathVariable("userId") int userId,
                                 @ModelAttribute UserDTO userDTO,
                                 Model model) {
        model.addAttribute(userDTO);
        model.addAttribute(userId);
        return "adminUserUpdateForm";
    }

    @PostMapping("/user/update/{userId}")
    public String updateUser(@PathVariable("userId") int userId,
                             @ModelAttribute UserDTO userDTO) {
        boolean update = adminService.updateUser(userDTO, userId);
        if (update) {
            return "redirect:/admin/user/"+userId;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/user/disable/{userId}")
    public String disableUser(@PathVariable("userId") int userId) {
        boolean disable = adminService.disableUser(userId);
        if (disable) {
            return "redirect:/admin/users";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/user/enable/{userId}")
    public String enableUser(@PathVariable("userId") int userId) {
        boolean enable = adminService.enableUser(userId);
        if (enable) {
            return "redirect:/admin/users";
        } else {
            return "errorUpdate";
        }
    }
}
