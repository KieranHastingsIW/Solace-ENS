package com.restsolace.solacerest.Repository;
// import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.ArrayList; 






public class HospitalEmailRepository{

    public List<String> findAll(){
        ArrayList<String> emailList = new ArrayList<String>(); 
        emailList.add("kiehas@gmail.com");                            
        emailList.add("fluxblizard@gmail.com");                                                     
        emailList.add("email4");                            
        emailList.add("email5");                            
        List<String> rList = emailList;
        return rList;

    }
}
