package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CategoryService - 카테고리 비즈니스 로직 계층
 *
 * 클래스 레벨 @Transactional(readOnly = true):
 *   - 기본적으로 모든 메서드를 읽기 전용 트랜잭션으로 실행합니다.
 *   - 쓰기가 필요한 메서드에만 @Transactional을 추가해 readOnly를 오버라이드합니다.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 전체 카테고리 목록 조회
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 카테고리 등록
     * - 이름 중복 시 DuplicateCategoryException 발생
     *
     * @Transactional: readOnly 기본값을 false로 오버라이드 (쓰기 허용)
     */
    @Transactional
    public Category createCategory(String name) {
        // 이름 중복 검사: 이미 존재하면 예외 발생
        categoryRepository.findByName(name)
                .ifPresent(c -> { throw new DuplicateCategoryException(name); });

        return categoryRepository.save(new Category(name));
    }

    /**
     * 카테고리 삭제
     * - 연결된 상품이 있으면 IllegalStateException 발생
     */
    @Transactional
    public void deleteCategory(Long id) {
        long count = categoryRepository.countProductsByCategoryId(id);
        if (count > 0) {
            throw new IllegalStateException("상품 " + count + "개가 연결되어 있어 삭제할 수 없습니다.");
        }
        categoryRepository.delete(id);
    }
}
