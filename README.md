# 仿滴滴验证码输入框

------

**参考简书大佬:** [Android仿滴滴出行验证码输入框效果][1]

实现效果如下：

![cmd-markdown-logo](https://upload-images.jianshu.io/upload_images/4764069-f3c76c71fda8aabb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> * 自动跳入下一个输入框
> * 输入完成获取输入内容
> * 宽度动态改变
> * 宽高一致
> * 设置边距,使用padding来完成


------

## 1. 引入方式:

```Gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```Gradle
dependencies {
	        implementation 'com.github.chengamin:VerificationCodeInputLayout:1.0.1'
	}
```

引入完毕,下面开始讲解使用方式

------
## 2. 使用方式:

xml文件中:
```xml
 <com.rj.library.VerificationCodeInputLayout
        android:id="@+id/verificationCodeInputLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="30dp"
        android:paddingBottom="30dp"
        app:boxCount="6"
        app:textSize="12sp"
        app:textColor="#333333"
        app:boxDrawableFocus="@drawable/verification_edit_bg_focus"
        app:boxDrawableNormal="@drawable/verification_edit_bg_normal"
        app:boxSpacing="10dp"
        app:inputType="number"
        ></com.rj.library.VerificationCodeInputLayout>

```

java文件中使用:

```java
VerificationCodeInputLayout verificationCodeInputLayout  = findViewById(R.id.verificationCodeInputLayout);
        verificationCodeInputLayout.setOnInputCompleteListener(new VerificationCodeInputLayout.onInputCompleteListener() {
            @Override
            public void onInputComplete(String content) {
                Toast.makeText(MainActivity.this, "content="+content,                     Toast.LENGTH_SHORT).show();
            }
        });
```

### 4. xml字段声明![1

| 属性        | 作用   |  注意事项  |
| --------   | -----:  | :----:  |
| boxCount     | 设置输入框数量 |   默认为6     |
| boxSpacing        |   设置输入框间距   |   默认为8   |
| boxDrawableNormal        |    设置输入框没有焦点的时候的背景    | |
| boxDrawableFocus        |    设置输入框有焦点的时候的背景    |    |
| textSize        |    设置输入框文字大小    |  默认为14  |
| textColor        |    设置输入框文字颜色    |  默认为黑色  |
| inputType        |    设置输入框类型:number,text,password,phone    |  默认为数字  |

### 5. 还未加入限制数字的使用方式....


  [1]: https://www.jianshu.com/p/e28a2942a4bc "Android仿滴滴出行验证码输入框效果"
