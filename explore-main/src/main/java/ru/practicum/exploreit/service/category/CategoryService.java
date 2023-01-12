package ru.practicum.exploreit.service.category;

import ru.practicum.exploreit.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories(Integer from, Integer size);

    Category getCategoryById(long catId);

    List<Category> getCategoryListById(List<Long> ids);

    Category createCategory(Category category);

    void deleteCategory(Long catId);

    Category updateCategory(Category category);
}
