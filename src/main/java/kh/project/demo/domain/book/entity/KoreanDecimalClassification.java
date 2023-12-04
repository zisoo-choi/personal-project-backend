package kh.project.demo.domain.book.entity;

public enum KoreanDecimalClassification {
    PHILOSOPHY("철학"),
    RELIGION("종교"),
    SOCIAL_SCIENCE("사회과학"),
    NATURAL_SCIENCE("자연과학"),
    TECHNOLOGY("기술과학"),
    ART("예술"),
    LANGUAGE("언어"),
    LITERATURE("문학"),
    HISTORY("역사");

    private String classificationName;

    KoreanDecimalClassification(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getClassificationName() {
        return classificationName;
    }
}
