package com.blog.services;

import com.blog.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {
    //create
    public CategoryDto createCategory(CategoryDto categoryDto);
    //update
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catagoryId);
    //delete
    public void deleteCategory(Integer catagoryId);
    //get
    public CategoryDto getCategoryById(Integer catagoryId);
    //getAll
    public List<CategoryDto> getAllCategory();

}
