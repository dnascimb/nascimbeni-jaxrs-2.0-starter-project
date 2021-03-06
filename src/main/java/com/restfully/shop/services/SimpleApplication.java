package com.restfully.shop.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class SimpleApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();

   public SimpleApplication()
   {
      singletons.add(new CustomerResource());
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }
}
