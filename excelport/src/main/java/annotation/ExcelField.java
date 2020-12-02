package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author:tyy
 * @date:2020/12/1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
    /**
     *导出字段标题
     */
    String title();

    /**
     * 导出字段排序
     */
    int sort() default 0;

    /**
     * 对齐方式（0：自动，1：靠左，2：居中；3：靠右）
     */
    int align() default 0;
}
