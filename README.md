##用法：

1. 直接注入Service：

  ```java
  private Mapper<User> mapper;
  ```

2. 继承接口

  ```java
  public interface UserMapper extends Mapper<User>
  ```

3. 直接使用

  ```java
  private Mapper mapper;
  ```