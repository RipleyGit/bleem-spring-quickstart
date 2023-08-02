package site.bleem.boot.event.org;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.event.TransactionPhase;

import java.lang.annotation.*;

// 在这个注解上面有一个注解：`@EventListener`，所以表明其实这个注解也是个事件监听器。
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener
public @interface TransactionalEventListener {

 /**
  * 这个注解取值有：BEFORE_COMMIT(指定目标方法在事务commit之前执行)、AFTER_COMMIT(指定目标方法在事务commit之后执行)、
  * AFTER_ROLLBACK(指定目标方法在事务rollback之后执行)、AFTER_COMPLETION(指定目标方法在事务完成时执行，这里的完成是指无论事务是成功提交还是事务回滚了)
  * 各个值都代表什么意思表达什么功能，非常清晰，
  * 需要注意的是：AFTER_COMMIT + AFTER_COMPLETION是可以同时生效的
  * AFTER_ROLLBACK + AFTER_COMPLETION是可以同时生效的
  */
 TransactionPhase phase() default TransactionPhase.AFTER_COMMIT;

 /**
  * 表明若没有事务的时候，对应的event是否需要执行，默认值为false表示，没事务就不执行了。
  */
 boolean fallbackExecution() default false;

 /**
  *  这里巧妙的用到了@AliasFor的能力，放到了@EventListener身上
  *  注意：一般建议都需要指定此值，否则默认可以处理所有类型的事件，范围太广了。
  */
 @AliasFor(annotation = EventListener.class, attribute = "classes")
 Class<?>[] value() default {};

 /**
  * The event classes that this listener handles.
  * <p>If this attribute is specified with a single value, the annotated
  * method may optionally accept a single parameter. However, if this
  * attribute is specified with multiple values, the annotated method
  * must <em>not</em> declare any parameters.
  */
 @AliasFor(annotation = EventListener.class, attribute = "classes")
 Class<?>[] classes() default {};

 /**
  * Spring Expression Language (SpEL) attribute used for making the event
  * handling conditional.
  * <p>The default is {@code ""}, meaning the event is always handled.
  * @see EventListener#condition
  */
 @AliasFor(annotation = EventListener.class, attribute = "condition")
 String condition() default "";

 /**
  * An optional identifier for the listener, defaulting to the fully-qualified
  * signature of the declaring method (e.g. "mypackage.MyClass.myMethod()").
  * @since 5.3
  * @see EventListener#id
  * @see TransactionalApplicationListener#getListenerId()
  */
 @AliasFor(annotation = EventListener.class, attribute = "id")
 String id() default "";

}