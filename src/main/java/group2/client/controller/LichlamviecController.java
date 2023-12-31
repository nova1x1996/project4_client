/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Lichlamviec;
import group2.client.entities.Patient;
import group2.client.repository.LichlamviecRepository;
import group2.client.service.AuthService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author hokim
 */
@Controller
public class LichlamviecController {

    String apiUrl = "http://localhost:8888/api/lichlamviec/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";

    RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private LichlamviecRepository lichlamviecRepository;

    @RequestMapping("/admin/lichlamviec")
    public String page(Model model, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
             ResponseEntity<List<Lichlamviec>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lichlamviec>>() {
            });
             
              ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful() && DResponse.getStatusCode().is2xxSuccessful()) {
                List<Lichlamviec> listLich = response.getBody();
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
               
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listLich", listLich);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "/admin/lichlamviec/index";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
             ResponseEntity<List<Lichlamviec>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lichlamviec>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Lichlamviec> listLich = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listLich", listLich);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "/admin/lichlamviec/index";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             ResponseEntity<List<Lichlamviec>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lichlamviec>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Lichlamviec> listLich = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listLich", listLich);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/lichlamviec/index";
        }else {
            return "redirect:/login";
        }
        
       
    }

    @RequestMapping("/admin/lichlamviec/create")
    public String create(Model model, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
             ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
            });
             
           

            if (DResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentAdmin", currentAdmin);
                // Chuyển hướng về trang danh sách Casher

            } 
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("lich", new Lichlamviec());
            return "/admin/lichlamviec/create";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentDoctor", currentDoctor);
                // Chuyển hướng về trang danh sách Casher

            } 
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("lich", new Lichlamviec());
            return "/admin/lichlamviec/create";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentCasher", currentCasher);
                // Chuyển hướng về trang danh sách Casher

            } 
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("lich", new Lichlamviec());
            return "/admin/lichlamviec/create";
        }else {
            return "redirect:/login";
        }
        
       
    }

    @RequestMapping(value = "/admin/lichlamviec/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Lichlamviec lich, HttpSession session, @RequestParam("doctor") String doctorID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        
        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        lich.setDoctorId(newDoctor);
        
        if(timeSelect.equals("7:00 - 12:00")){
            lich.setStarttime("7:00");
            lich.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            lich.setStarttime("12:00");
            lich.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            lich.setStarttime("17:00");
            lich.setEndtime("21:00");
        }
        
        String[] parts = timeSelect.split("-");
        
        Lichlamviec checklichlamviecByDoctorAndDateAndTime = lichlamviecRepository.findByDoctorIdAndDateAndStarttimeAndEndtime(newDoctor, lich.getDate(), parts[0], parts[1]);
        
        if(checklichlamviecByDoctorAndDateAndTime != null){
            session.setAttribute("checklichlamviec", "It is not possible to schedule the same doctor for the same day and time. Please choose another doctor, date and time");
            return "redirect:/admin/lichlamviec/create";
        }else{
            // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
            HttpEntity<Lichlamviec> request = new HttpEntity<>(lich, headers);

            ResponseEntity<Lichlamviec> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Lichlamviec.class);

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                // Chuyển hướng về trang danh sách Casher
                 model.addAttribute("lich", new Lichlamviec());
                 session.setAttribute("msg", "Create work schedule successful!");
                return "redirect:/admin/lichlamviec";
            } else {
                // Xử lý lỗi nếu cần thiết
                return "/admin/lichlamviec/create";
            }
        }
        
    }

    @RequestMapping(value = "/admin/lichlamviec/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
              Lichlamviec lich = restTemplate.getForObject(apiUrl + "/" + id, Lichlamviec.class);
    //        model.addAttribute("lich", lich);
             ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful()) {

                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("lich", lich);
                model.addAttribute("currentAdmin", currentAdmin);
                return "/admin/lichlamviec/edit";
            } else {
                return "redirect:/admin/lichlamviec";
            }
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
              Lichlamviec lich = restTemplate.getForObject(apiUrl + "/" + id, Lichlamviec.class);
    //        model.addAttribute("lich", lich);
             ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful()) {

                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("lich", lich);
                model.addAttribute("currentDoctor", currentDoctor);
                return "/admin/lichlamviec/edit";
            } else {
                return "redirect:/admin/lichlamviec";
            }
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
               Lichlamviec lich = restTemplate.getForObject(apiUrl + "/" + id, Lichlamviec.class);
    //        model.addAttribute("lich", lich);
             ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful()) {

                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("lich", lich);
                model.addAttribute("currentCasher", currentCasher);
                return "/admin/lichlamviec/edit";
            } else {
                return "redirect:/admin/lichlamviec";
            }
        }else {
            return "redirect:/login";
        }
        
      
    }

    @RequestMapping(value = "/admin/lichlamviec/edit/{id}", method = RequestMethod.POST)
    public String update(Model model, @PathVariable("id") Integer id, @ModelAttribute Lichlamviec updatedLich, HttpSession session, @RequestParam("DoctorID") String doctorID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Lichlamviec existingLich = restTemplate.getForObject(apiUrl + "/" + updatedLich.getId(), Lichlamviec.class);

        Doctor newD = new Doctor();
        newD.setId(Integer.parseInt(doctorID));
        updatedLich.setDoctorId(newD);
        
         if(timeSelect.equals("7:00 - 12:00")){
            updatedLich.setStarttime("7:00");
            updatedLich.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            updatedLich.setStarttime("12:00");
            updatedLich.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            updatedLich.setStarttime("17:00");
            updatedLich.setEndtime("21:00");
        }
        
        String[] parts = timeSelect.split("-");
        
        Lichlamviec checklichlamviecByDoctorAndDateAndTime = lichlamviecRepository.findByDoctorIdAndDateAndStarttimeAndEndtime(newD, updatedLich.getDate(), parts[0], parts[1]);
        
        if(checklichlamviecByDoctorAndDateAndTime != null){
            session.setAttribute("checklichlamviec", "Không thể tạo lịch cho cùng một bác sĩ trong cùng ngày cùng giờ được. Vui lòng chọn bác sĩ, ngày và giờ khác");
            return "redirect:/admin/lichlamviec/edit/{id}";
        }else{
             // Bổ sung id vào URL khi thực hiện PUT
            String url = apiUrl + "/" + updatedLich.getId();

            // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
            HttpEntity<Lichlamviec> request = new HttpEntity<>(updatedLich, headers);

            try {
                restTemplate.exchange(url, HttpMethod.PUT, request, Lichlamviec.class);
                session.setAttribute("msg", "Update work schedule successful!");
                return "redirect:/admin/lichlamviec";

            } catch (RestClientException e) {
                model.addAttribute("lich", existingLich);
                return "/admin/lichlamviec/edit";
            }
        }
        
       
    }

    @RequestMapping(value = "/admin/lichlamviec/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id, HttpServletRequest request, HttpSession session) {
        
         Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete work schedule successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/lichlamviec";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
             restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete work schedule successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/lichlamviec";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete work schedule successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/lichlamviec";
        }else {
            return "redirect:/login";
        }
        
        
    }

   
     @RequestMapping(value = "/admin/lichlamviec/search", method = RequestMethod.GET)
    public String searchByDoctor(Model model, @RequestParam("lichlamviec-doctor") String lichlamviec_doctor, HttpServletRequest request){
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
             return "redirect:/forbien";
         } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
                ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
            });
             
           List<Lichlamviec> listLich = lichlamviecRepository.searchLich(lichlamviec_doctor);
           
            if (DResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listLich", listLich);
                model.addAttribute("currentAdmin", currentAdmin);
            }

            return "/admin/lichlamviec/index";
         }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
              
                model.addAttribute("currentDoctor", currentDoctor);

                return "/admin/lichlamviec/index";
         }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             
                model.addAttribute("currentCasher", currentCasher);

                return "/admin/lichlamviec/index";
         }else {
            return "redirect:/login";
        }
      
       
    }
    
    
       @RequestMapping(value = "/admin/lichlamviec/search-each-day", method = RequestMethod.GET)
    public String searchScheduleEachDay(Model model, @RequestParam("llv-date") String selectDate, HttpServletRequest request) throws ParseException {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
             return "redirect:/forbien";
         } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
          

               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date choose_date = sdf.parse(selectDate);
             
                List<Lichlamviec> scheduleByDate = lichlamviecRepository.findByDate(choose_date);
                model.addAttribute("listLich", scheduleByDate);
                model.addAttribute("currentAdmin", currentAdmin);

                return "/admin/lichlamviec/index";
         }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
             
               
                model.addAttribute("currentDoctor", currentDoctor);
                return "/admin/lichlamviec/index";
         }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
              
                model.addAttribute("currentCasher", currentCasher);

                return "/admin/lichlamviec/index";
         }else {
            return "redirect:/login";
        }

    }

}

