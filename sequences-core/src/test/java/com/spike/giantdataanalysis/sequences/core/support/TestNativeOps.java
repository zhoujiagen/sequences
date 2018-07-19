package com.spike.giantdataanalysis.sequences.core.support;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.spike.giantdataanalysis.sequences.core.locking.PCB;
import com.spike.giantdataanalysis.sequences.core.locking.lock.LOCK_MODE;
import com.spike.giantdataanalysis.sequences.core.locking.semaphore;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CUPtr;

public class TestNativeOps {

  public static void main(String[] args) {
    // atomicRef();
    // atomicField();
    // CSO();
    CSP();
  }

  static void CSP() {

    semaphore cellvalue = new semaphore();
    cellvalue.mode = LOCK_MODE.LOCK_IS;
    cellvalue.count = 0;
    cellvalue.wait_list = PCB.NULL();
    semaphore oldvalue = new semaphore();
    oldvalue.mode = LOCK_MODE.LOCK_IS;
    oldvalue.count = 0;
    oldvalue.wait_list = PCB.NULL();

    semaphore _newvalue = new semaphore();
    _newvalue.mode = LOCK_MODE.LOCK_S;
    _newvalue.count = 0;
    _newvalue.wait_list = PCB.NULL();

    System.out.println(cellvalue);
    System.out.println(oldvalue);
    System.out.println(_newvalue);

    boolean result = NativeOps.CSP(CUPtr.of(cellvalue), CUPtr.of(oldvalue), CUPtr.of(_newvalue));
    System.out.println(result);

    System.out.println(cellvalue);
    System.out.println(oldvalue);
    System.out.println(_newvalue);
  }

  static void CSO() {
    semaphore cellvalue = new semaphore();
    cellvalue.mode = LOCK_MODE.LOCK_IS;
    cellvalue.count = 0;
    cellvalue.wait_list = PCB.NULL();
    semaphore.semaphoreSwapableObject cell = new semaphore.semaphoreSwapableObject(cellvalue);
    semaphore oldvalue = new semaphore();
    oldvalue.mode = LOCK_MODE.LOCK_IS;
    oldvalue.count = 0;
    oldvalue.wait_list = PCB.NULL();
    semaphore.semaphoreSwapableObject old = new semaphore.semaphoreSwapableObject(oldvalue);

    semaphore _newvalue = new semaphore();
    _newvalue.mode = LOCK_MODE.LOCK_S;
    _newvalue.count = 0;
    _newvalue.wait_list = PCB.NULL();
    semaphore.semaphoreSwapableObject _new = new semaphore.semaphoreSwapableObject(_newvalue);

    System.out.println(cell);
    System.out.println(old);
    System.out.println(_new);

    boolean result = NativeOps.CSO(cell, old, _new);
    System.out.println(result);

    System.out.println(cell);
    System.out.println(old);
    System.out.println(_new);
  }

  static void atomicField() {
    AtomicReferenceFieldUpdater<User, Address> arfu =
        AtomicReferenceFieldUpdater.newUpdater(User.class, Address.class, "address");
    User user = new User("user1", 1);
    Address address = new Address("street1", "city1");
    arfu.set(user, address);

    System.out.println(arfu.get(user));
    System.out.println(user + ", " + user.asString());

    Address address2 = new Address("street2", "city2");
    boolean result = arfu.compareAndSet(user, address, address2);
    System.out.println(result);
    System.out.println(arfu.get(user));
    System.out.println(user + ", " + user.asString());

  }

  static void atomicRef() {
    AtomicReference<User> ar = new AtomicReference<>();

    User user = new User("user1", 1);
    User user2 = new User("user2", 2);
    System.out.println(user + ", " + user.hashCode() + ", " + user.asString());
    System.out.println(user2 + ", " + user2.hashCode() + ", " + user2.asString());

    ar.set(user);
    System.out.println("in ar: " + ar.get() + ", " + ar.get().hashCode());

    boolean result = ar.compareAndSet(user, user2);

    System.out.println(result);
    System.out.println("in ar: " + ar.get() + ", " + ar.get().hashCode());

    System.out.println(user + ", " + user.hashCode() + ", " + user.asString());
    System.out.println(user2 + ", " + user2.hashCode() + ", " + user2.asString());
  }

  static class Address {
    public String street;
    public String city;

    public Address() {
    }

    public Address(String street, String city) {
      this.street = street;
      this.city = city;
    }

    public String getStreet() {
      return street;
    }

    public void setStreet(String street) {
      this.street = street;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    @Override
    public String toString() {
      return "Address [street=" + street + ", city=" + city + "]";
    }
  }

  static class User {
    public String name;
    public int age;
    public volatile Address address;

    public User() {
    }

    public User(String name, int age) {
      super();
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public String asString() {
      return "User [name=" + name + ", age=" + age + ", address=" + address + "]";
    }

  }
}
