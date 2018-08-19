package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 19/08/2018
 * @Class purposes : .......
 */

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView notFoundHandle(Exception e) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("error", e.getMessage());
    mv.setViewName(ViewNames.ERROR_PAGE_NOT_FOUND);
    return mv;
  }


  @ExceptionHandler(NumberFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView badFormatHandle(Exception e) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("error", e.getMessage());
    mv.setViewName(ViewNames.ERROR_PAGE_BAD_REQUEST);
    return mv;
  }
}
