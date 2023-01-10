package ru.practicum.exploreit.service.category;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreit.exception.ErrorDataHandlingException;
import ru.practicum.exploreit.extention.pagination.PaginationParams;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories(Integer from, Integer size) {
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        log.info("Получен запрос на получение всех категорий.");
        return categoryRepository.findAll(pageRequest).toList();
    }

    @Override
    public Category getCategoryById(long catId) {
        log.info("Получен запрос на получение категории с id = {}", catId);
        Optional<Category> categoryOptional = categoryRepository.findById(catId);
        if (categoryOptional.isEmpty()) {
            log.error("Ошибка при поиске категории с catId: {}", catId);
            throw new ErrorDataHandlingException("Ошибка при поиске пользователя!");
        } else {
            return categoryOptional.get();
        }
    }

    @Override
    public List<Category> getCategoryListById(List<Long> ids) {
        return categoryRepository.findAllByIdIn(ids);
    }

    @Override
    public Category createCategory(Category category) {
        log.info("Получен запрос на создание категории {}", category);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        log.info("Получен запрос на удаление категории с id = {}", catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public Category updateCategory(Category category) {
        log.info("Получен запрос на обновление категории {}", category);
        return categoryRepository.save(category);
    }
}
