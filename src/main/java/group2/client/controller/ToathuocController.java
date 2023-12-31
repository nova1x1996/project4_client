package group2.client.controller;

import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListToaThuocDAO;
import group2.client.dto.ToaThuocDAO;
import group2.client.entities.*;
import group2.client.service.AuthService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/admin/toathuoc")
public class ToathuocController {
    private String apiUrl_Toathuoc = "http://localhost:8888/api/toathuoc/";
    private String apiUrl_taophieukham = "http://localhost:8888/api/taophieukham/typeDoctorId/";
    private String apiUrl = "http://localhost:8888/api/taophieukham/";
    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private AuthService authService;
    @RequestMapping("")
    public String page(Model model, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if(currentPatient != null && currentPatient.getRole().equals("PATIENT")){
            return "redirect:/forbien";
        }else if(currentAdmin != null && currentAdmin.getRole().equals("ADMIN")){
             ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Toathuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/toathuoc/index";
        }else if(currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")){
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Toathuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "admin/toathuoc/index";
        }else if(currentCasher != null && currentCasher.getRole().equals("CASHER")){
             ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Toathuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentCasher", currentCasher);

            }
            return "admin/toathuoc/index";
        }else {
            return "redirect:/login";
        }
        
       
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc, @PathVariable("id") Integer phieukhamId, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if(currentPatient != null && currentPatient.getRole().equals("PATIENT")){
            return "redirect:/forbien";
        }else if(currentAdmin != null && currentAdmin.getRole().equals("ADMIN")){
            //Lấy List Type Donthuoc
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
                    });
            List<Toathuoc> listToathuoc = response.getBody();

            model.addAttribute("listToathuoc", listToathuoc);
             model.addAttribute("currentAdmin", currentAdmin);
            model.addAttribute("toathuoc", new Toathuoc());
            return "admin/toathuoc/create";
        }else if(currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")){
            //Lấy List Type Donthuoc
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
                    });
             List<Toathuoc> listToathuoc = response.getBody();


             // Lấy thông tin phiếu khám từ API bằng phieukhamId
             ResponseEntity<Taophieukham> response1 = restTemplate.exchange(apiUrl + "/" + phieukhamId, HttpMethod.GET, null,
                     new ParameterizedTypeReference<Taophieukham>() {});
             Taophieukham taophieukham = response1.getBody();
            model.addAttribute("listToathuoc", listToathuoc);
             model.addAttribute("phieukham", taophieukham);
             model.addAttribute("currentDoctor", currentDoctor);
            model.addAttribute("toathuoc", new Toathuoc());
            return "admin/toathuoc/create";
        }else if(currentCasher != null && currentCasher.getRole().equals("CASHER")){
             //Lấy List Type Donthuoc
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
                    });
            List<Toathuoc> listToathuoc = response.getBody();
            model.addAttribute("listToathuoc", listToathuoc);
             model.addAttribute("currentCasher", currentCasher);
            model.addAttribute("toathuoc", new Toathuoc());
            return "admin/toathuoc/create";
        }else {
            return "redirect:/login";
        }

        
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, HttpServletRequest request, int[] thuocID, int[] quantity, int[] sang, int[] trua, int[] chieu, int[] toi, String sympton, int phieukhamId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngaytaikham) {
        List<ToaThuocDAO> list = new ArrayList<ToaThuocDAO>();
        Set<Integer> seenThuoIDs = new HashSet<>(); // Sử dụng Set để theo dõi thuocID đã xuất hiện

        for(int i = 0; i < thuocID.length; i++) {
            int currentThuocID = thuocID[i];

            // Nếu thuocID hiện tại chưa xuất hiện, thêm vào danh sách và đánh dấu đã xuất hiện
            if (!seenThuoIDs.contains(currentThuocID)) {
                ToaThuocDAO obj = new ToaThuocDAO(currentThuocID, quantity[i], sang[i], trua[i], chieu[i], toi[i]);
                list.add(obj);
                seenThuoIDs.add(currentThuocID);
            }
        }
        // Authenticate the doctor and retrieve their information
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Integer doctorId = currentDoctor.getId();


        ListToaThuocDAO listTT = new ListToaThuocDAO();
        listTT.setListTT(list);

        listTT.setSymptom(sympton);
        listTT.setDoctorId(doctorId); // Set the doctorId
        listTT.setTaophieukhamId(phieukhamId);
        listTT.setNgaytaikham(ngaytaikham);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Toathuoc để gửi yêu cầu POST
        HttpEntity<ListToaThuocDAO> requestEntity = new HttpEntity<>(listTT, headers);

        ResponseEntity<ListToaThuocDAO> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.POST, requestEntity, ListToaThuocDAO.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Toathuoc thành công (nếu cần)

            // Chuyển hướng về trang danh sách Toathuoc
            return "redirect:/admin/toathuoc";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "redirect:/admin/toathuoc";
        }
    }


    @RequestMapping(value = "/export-pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportToPDF(@RequestParam("toathuocId") int toathuocId) {
        
        if (toathuocId <= 0) {
            // Xử lý lỗi nếu tham số không hợp lệ
            return ResponseEntity.badRequest().body("Invalid toathuocId".getBytes());
        }

        // Gọi API từ máy chủ backend để lấy tài liệu PDF
        ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl_Toathuoc + "/export-pdf?toathuocId=" + toathuocId, HttpMethod.GET, null, byte[].class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] pdfBytes = response.getBody();

            // Thiết lập header và trả về file PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "toathuoc.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } else {
            // Xử lý lỗi nếu không thể lấy tài liệu PDF từ API
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] createToathuocPDF(Toathuoc toathuoc) throws IOException {
        // Tạo một tài liệu PDF mới
        PDDocument document = new PDDocument();

        // Tạo một trang mới cho tài liệu
        PDPage page = new PDPage();
        document.addPage(page);

        // Tạo một luồng nội dung cho trang
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Thực hiện viết nội dung PDF ở đây (ví dụ: ghi thông tin từ toathuoc vào tài liệu)

        // Đóng luồng nội dung
        contentStream.close();

        // Lưu trang và đóng tài liệu
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}