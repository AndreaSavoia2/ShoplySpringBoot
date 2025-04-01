package com.project.shoply.service;

import com.project.shoply.entity.Brand;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public String saveBrand(String mame) {
        String brandName = mame.trim().toLowerCase();
        if (brandRepository.existsByName(brandName))
            throw new GenericException("Brand " + brandName + " already exist",
                    HttpStatus.CONFLICT);
        Brand brand = new Brand(brandName);
        brandRepository.save(brand);
        return "Brand saved";
    }

    @Transactional
    public String updateBrand(String newName, long brandId) {
        String newBrandName = newName.trim().toLowerCase();
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", brandId));
        if (brandRepository.existsByNameAndIdNot(newBrandName, brandId ))
            throw new GenericException("Brand " + newBrandName + " already exist",
                    HttpStatus.CONFLICT);
        brand.setName(newBrandName);
        return "Brand update";
    }

    public List<Brand> getAllBrands(){
        return brandRepository.findAll();
    }

    protected Brand findBrandByName(String name) {
        return brandRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "name", name));
    }
}
