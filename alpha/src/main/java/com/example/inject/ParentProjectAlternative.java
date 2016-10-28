package com.example.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;

/**
 * Stereotype annotation to mark @Alternative implementations provided by the
 * parent project which should be used instead of default implementations
 * provided by the subproject.
 * 
 * See
 * {@link http://docs.jboss.org/weld/reference/latest/en-US/html/stereotypes.html}
 * 
 * This allows us to use annotations instead of beans.xml config (see
 * {@link http://docs.jboss.org/weld/reference/latest/en-US/html/injection.html#alternatives}
 * ), providing better coding practice (keeping relevant code close).
 *
 */
@Alternative
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
public @interface ParentProjectAlternative {

}
