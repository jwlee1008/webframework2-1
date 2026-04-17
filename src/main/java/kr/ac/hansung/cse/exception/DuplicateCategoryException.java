package kr.ac.hansung.cse.exception;

/**
 * DuplicateCategoryException - 카테고리 이름 중복 시 발생하는 커스텀 예외
 * CategoryService.createCategory()에서 이름 중복을 감지할 때 사용합니다.
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("이미 존재하는 카테고리입니다: " + name);
    }
}
