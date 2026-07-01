package com.project.moneymanager.service;
import com.project.moneymanager.dto.CategoryDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryServices {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProfileServices profileServices;

    public CategoryDto saveCategory(CategoryDto categoryDto) {
        ProfileEntity profile = profileServices.getCurrentProfile();
        if (
            categoryRepository.existsByNameAndProfileId(
                categoryDto.getName(),
                profile.getId()
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Category with this name already exist"
            );
        }
        CategoryEntity categoryEntity = toEntity(categoryDto, profile);
        categoryRepository.save(categoryEntity);
        return toDto(categoryEntity);
    }

    public CategoryEntity toEntity(
        CategoryDto categoryDto,
        ProfileEntity profileEntity
    ) {
        return CategoryEntity.builder()
            .name(categoryDto.getName())
            .icon(categoryDto.getIcon())
            .type(categoryDto.getType())
            .profile(profileEntity)
            .build();
    }

    public List<CategoryDto> getAllCategoryForCUrrentUser()
    {
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<CategoryEntity> categoryEntities =
            categoryRepository.findByProfileId(profile.getId());
        return categoryEntities.stream().map(this::toDto).toList();

    }

    public CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
            .id(categoryEntity.getId())
            .name(categoryEntity.getName())
            .type(categoryEntity.getType())
            .profileId(
                categoryEntity.getProfile() != null
                    ? categoryEntity.getProfile().getId()
                    : null
            )
            .createdAt(categoryEntity.getCreatedAt())
            .updatedAt(categoryEntity.getUpdatedAt())
            .icon(categoryEntity.getIcon())
            .build();
    }

    public List<CategoryDto> getCategoriesBasedOnType(String type) {
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<CategoryEntity> categoryEntityList =
            categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categoryEntityList.stream().map(this::toDto).toList();
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        System.out.println("Updating category id: " + id);
        System.out.println("CategoryDto: " + categoryDto.getName());

        ProfileEntity profile = profileServices.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository
            .findByIdAndProfileId(id, profile.getId())
            .orElseThrow(() ->
                new RuntimeException("category of these id not found")
            );

        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setIcon(categoryDto.getIcon());
        categoryEntity.setType(categoryDto.getType());
        categoryRepository.save(categoryEntity);
        return toDto(categoryEntity);
    }
}
