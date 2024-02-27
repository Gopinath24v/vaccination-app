package com.jpa.vaccinationapp.vaccinationCenter.service;

import com.jpa.vaccinationapp.admin.Admin;
import com.jpa.vaccinationapp.vaccinationCenter.AddressDTO;
import com.jpa.vaccinationapp.vaccinationCenter.Center;
import com.jpa.vaccinationapp.vaccinationCenter.CenterException;
import com.jpa.vaccinationapp.vaccinationCenter.CenterRepository;
import com.jpa.vaccinationapp.vaccine.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    @Autowired
    public CenterServiceImpl(CenterRepository centerRepository){
        this.centerRepository=centerRepository;
    }

    @Override
    public Center createCenter(Center newCenter) throws CenterException {
        return centerRepository.save(newCenter);
    }

    @Override
    public Center removeCenter(Integer centerID) throws CenterException {
        Optional<Center>center= centerRepository.findById(centerID);
        if(center.isEmpty()){
            String message=String.format("There is no such centre with ID: %d to remove",centerID);
            throw new CenterException(message);
        }
        centerRepository.delete(center.get());
        return center.get();
    }

    @Override
    public Center updateCenter(Center center) throws CenterException {
        Optional<Center>result= centerRepository.findById(center.getCenterId());
        if(result.isEmpty()){
            String message=String.format("There is no such centre with ID: %d to update",center.getCenterId());
            throw new CenterException(message);
        }
        return centerRepository.save(result.get());
    }
    @Override
    public Center addVaccineToCenter(Integer centerID, Vaccine newVaccine) throws CenterException {
        if(newVaccine==null){
            throw new CenterException("Vaccine details can't be NULL");
        }
        Optional<Center>center= centerRepository.findById(centerID);
        if(center.isEmpty()){
            String message=String.format("Can't add a vaccine to centre! There is no such centre with ID: %d",
                    centerID);
            throw new CenterException(message);
        }
        center.get().getVaccineMap().add(newVaccine);
        centerRepository.save(center.get());
        return center.get();
    }
    @Override
    public Center removeVaccineFromCentre(Integer centerID, Vaccine vaccine) throws CenterException {
        if(vaccine==null){
            throw new CenterException("Vaccine details can't be NULL");
        }
        Optional<Center>center= centerRepository.findById(centerID);
        if(center.isEmpty()){
            String message=String.format("There is no such centre with ID: %d",centerID);
            throw new CenterException(message);
        }
        center.get().getVaccineMap().remove(vaccine.getVaccineId());

        centerRepository.save(center.get());
        return center.get();
    }

    @Override
    public List<Center> findCenterByCenterNameIsContainingIgnoreCase(String centerName) throws CenterException {
        if(centerName==null){
            throw new CenterException("Cannot perform search with no centre name");
        }
        Optional<List<Center>> center= Optional.ofNullable(centerRepository.
                findCenterByCenterNameIsContainingIgnoreCase(centerName));
        if(center.get().isEmpty()){
            String message=String.format("There's no such centre with name: %s. Please check it and try again",
                    centerName);
            throw new CenterException(message);
        }
        return center.get();
    }

    @Override
    public Center findByID(Integer centerID) throws CenterException {
        Optional<Center> center=centerRepository.findById(centerID);
        if(center.isEmpty()){
            String message=String.format("There is no such centre with ID: %d", centerID);
            throw new CenterException(message);
        }
        return center.get();
    }

    @Override
    public List<Center> findByPincode(String pincode) throws CenterException {
        if(pincode==null){
            throw new CenterException("Cannot perform search with no pincode");
        }
        var center = Optional.ofNullable(centerRepository.findByPincode(pincode));
        if(center.get().isEmpty()){
            String message=String.format("There's no such centre with the pincode: %s. " +
                    "Please check it and try again", pincode);
            throw new CenterException(message);
        }
        return center.get();
    }

    @Override
    public List<Center> getAllCenter(){
        return centerRepository.findAll();
    }

    @Override
    public Center updateCenterAddressAndPhone(AddressDTO addressDTO) throws CenterException {
        Optional<Center> result=centerRepository.findById(addressDTO.getCenterId());
        if(result.isEmpty()){
            String message=String.format("There is no such centre with ID to update the address: %d",
                    addressDTO.getCenterId());
            throw new CenterException(message);
        }
        Center center=result.get();
        center.setAddress(addressDTO.getAddress());
        center.setContactNumber(addressDTO.getContactNumber());
        center.setPincode(addressDTO.getPincode());
        center.setDistrict(addressDTO.getDistrict());
        center.setState(addressDTO.getState());
        return centerRepository.save(center);
    }
}
