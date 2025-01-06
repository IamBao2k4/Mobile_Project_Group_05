# Mobile_Project_Group_05


---

## Giới thiệu

"**Mobile_Project_Group_05**" là một dự án Android phát triển bởi nhóm 5 với mục tiêu xây dựng một ứng dụng quản lý ảnh và video hoàn chỉnh, hỗ trợ người dùng lưu trữ, chỉnh sửa, nhận diện văn bản từ ảnh và quản lý video. 
Ứng dụng được thiết kế với giao diện thân thiện, các chức năng mạnh mẽ, và sử dụng các công nghệ hiện đại trong lập trình Android.


---


## Mục tiêu của dự án

1. **Cung cấp trải nghiệm quản lý ảnh/video toàn diện:**
   - Hỗ trợ người dùng tổ chức các album ảnh.
   - Xử lý các tác vụ chỉnh sửa và quản lý ảnh/video.

2. **Tăng cường tính linh hoạt và dễ sử dụng:**
   - Thiết kế giao diện trực quan, dễ tiếp cận với mọi đối tượng người dùng.

3. **Ứng dụng công nghệ hiện đại:**
   - Tích hợp các thư viện hỗ trợ như `PhotoEditor`, `Retrofit`, `SQLite` và `Glide`.


---


## Tính năng chính

1. **Quản lý ảnh:**
   - Xem danh sách ảnh trong album, hiển thị theo ngày hoặc theo album.
   - Đặt ảnh làm hình nền điện thoại.

2. **Nhận diện văn bản:**
   - Sử dụng công nghệ nhận diện văn bản từ ảnh (Text Recognition).
   - Xuất văn bản từ ảnh sang file.

3. **Quản lý video:**
   - Hiển thị danh sách video trong ứng dụng.
   - Người dùng có thể xem video chi tiết và thêm vào danh sách yêu thích.

4. **Giao diện thân thiện:**
   - Hỗ trợ Dark Mode/Light Mode.
   - Dễ dàng điều hướng qua các màn hình.

5. **Tính năng bảo mật và cá nhân hóa:**
   - Lưu trữ dữ liệu người dùng an toàn với SQLite.
   - Quản lý danh sách yêu thích và ảnh cá nhân.

6. **Tính năng nâng cao:**
   - Xóa ảnh trùng lặp bằng `IdentifyDuplicateImage` helper.
   - Lắng nghe sự kiện vuốt bằng `OnSwipeTouchListener`.

6. **Xóa nền ảnh:**
   - Sử dụng `RemoveBgAPI` để xóa nền ảnh tự động.
   - Người dùng chọn ảnh, API sẽ xử lý và trả về ảnh không nền.

7. **Chỉnh sửa ảnh:**
   - Chọn ảnh cần chỉnh sửa từ danh sách.
   - Các công cụ chỉnh sửa:
      - **Thêm văn bản**: Người dùng nhập văn bản và tùy chỉnh màu sắc, font chữ.
      - **Vẽ lên ảnh**: Hỗ trợ nhiều màu sắc và độ dày bút vẽ.
      - **Áp dụng bộ lọc**: Tăng độ sáng, thay đổi màu sắc, áp dụng hiệu ứng đặc biệt.


---


## Cấu trúc thư mục

```plaintext
.
|-- Activity
|   |-- AddImageActivity.java
|   |-- AlbumDetailActivity.java
|   |-- EditImageActivity.java
|   |-- ImageDetailActivity.java
|   |-- TextRecognitionActivity.java
|   |-- VideoDetailActivity.java
|
|-- Adapter
|   |-- AlbumAdapter.java
|   |-- ImageAdapter.java
|   |-- ImagesByDateAdapter.java
|
|-- API
|   |-- APIClient.java
|   |-- RemoveBgAPI.java
|
|-- Component
|   |-- AlbumClass.java
|   |-- ImageClass.java
|
|-- Fragment
|   |-- DeleteFragment.java
|   |-- FavoriteFragment.java
|   |-- HomeFragment.java
|   |-- PhotosFragment.java
|   |-- VideoFragment.java
|
|-- Helper
|   |-- IdentifyDuplicateImage.java
|   |-- OnSwipeTouchListener.java
|   |-- ReadMediaFromExternalStorage.java
|   |-- SQLiteDataBase.java
|
|-- MainActivity.java
```


---


### Mô tả từng thư mục:

#### 1. **Activity:**
- Chứa các màn hình chính của ứng dụng, tương ứng với các tác vụ cụ thể:
  - `AddImageActivity`: Cho phép người dùng thêm ảnh mới.
  - `AlbumDetailActivity`: Hiển thị chi tiết một album.
  - `EditImageActivity`: Cung cấp công cụ chỉnh sửa ảnh.
  - `ImageDetailActivity`: Hiển thị chi tiết một bức ảnh.
  - `TextRecognitionActivity`: Nhận diện văn bản từ ảnh.
  - `VideoDetailActivity`: Xem chi tiết và phát video.

#### 2. **Adapter:**
- Quản lý và hiển thị dữ liệu trong các `RecyclerView`:
  - `AlbumAdapter`: Hiển thị danh sách album.
  - `ImageAdapter`: Hiển thị danh sách ảnh trong một album.
  - `ImagesByDateAdapter`: Hiển thị ảnh theo ngày.

#### 3. **API:**
- Kết nối với các dịch vụ bên ngoài:
  - `APIClient`: Gửi và nhận yêu cầu API.
  - `RemoveBgAPI`: Tích hợp API xóa nền ảnh.

#### 4. **Component:**
- Các lớp đại diện cho đối tượng trong ứng dụng:
  - `AlbumClass`: Mô tả một album.
  - `ImageClass`: Mô tả một bức ảnh.

#### 5. **Fragment:**
- Các giao diện nhỏ trong ứng dụng:
  - `DeleteFragment`: Quản lý giao diện xóa ảnh.
  - `FavoriteFragment`: Hiển thị danh sách ảnh yêu thích.
  - `HomeFragment`: Giao diện chính của ứng dụng.
  - `PhotosFragment`: Hiển thị danh sách ảnh.
  - `VideoFragment`: Hiển thị danh sách video.

#### 6. **Helper:**
- Các lớp hỗ trợ chức năng:
  - `IdentifyDuplicateImage`: Phát hiện ảnh trùng lặp.
  - `OnSwipeTouchListener`: Xử lý thao tác vuốt.
  - `ReadMediaFromExternalStorage`: Đọc dữ liệu từ bộ nhớ ngoài.
  - `SQLiteDataBase`: Quản lý cơ sở dữ liệu SQLite.


---


## Các dependencies

Dự án sử dụng các thư viện và công nghệ sau:

- **ViewPager2:** Hiệu ứng lướt giữa các trang.
- **SQLite:** Lưu trữ dữ liệu người dùng và ảnh.
- **Glide:** Hiển thị ảnh.
- **PhotoEditor:** Công cụ chỉnh sửa ảnh.
- **Retrofit + Gson Converter:** Kết nối mạng và chuyển đổi JSON.
- **OkHttp:** HTTP client cho mạng.
- **Text Recognition:** Nhận diện văn bản từ ảnh.
- **ConstraintLayout:** Tạo giao diện linh hoạt.
- **JUnit:** Kiểm thử đơn vị.
- **Espresso:** Kiểm thử giao diện người dùng.



---


## Hướng dẫn cài đặt


### Yêu cầu hệ thống
- **Android Studio**: Phiên bản 2022.3 trở lên.
- **JDK**: Phiên bản 11 hoặc mới hơn.
- **Thiết bị Android**: API 21 (Lollipop) trở lên.


1. **Cài đặt môi trường phát triển:**
   - Cài đặt [Android Studio](https://developer.android.com/studio).

2. **Clone dự án:**
   - Mở terminal và gõ lệnh:
     ```bash
     git clone https://github.com/IamBao2k4/Mobile_Project_Group_05.git
     ```

3. **Mở dự án:**
   - Mở Android Studio, chọn "Open an existing Android Studio project", điều hướng đến thư mục dự án.

4. **Cài đặt dependencies:**
   - Đảm bảo các dependencies trong `build.gradle` đã được cài đặt.

5. **Chạy ứng dụng:**
   - Kết nối thiết bị Android hoặc sử dụng Android Emulator.


## Các lỗi phổ biến và cách xử lý

### **Lỗi không tải được ảnh**
- Kiểm tra quyền truy cập bộ nhớ trong file `AndroidManifest.xml`:
  ```xml
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  ```

### **Lỗi không gọi được API**
- Kiểm tra cấu hình API key trong file `APIClient`.

### **Ứng dụng bị crash khi chạy trên thiết bị cũ**
- Đảm bảo `minSdkVersion` trong file `build.gradle` được đặt đúng:
  ```gradle
  minSdkVersion 21
  ```

---


## Tài liệu tham khảo
- [Android Developer Documentation](https://developer.android.com/docs)
- [Glide Documentation](https://github.com/bumptech/glide)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [PhotoEditor Library](https://github.com/burhanrashid52/PhotoEditor)


---


## Cộng tác viên

- **Phan Minh Gia Bảo** - Trưởng nhóm.
- **Lê Bảo** - Thành viên.
- **Trần Xuân Đăng** - Thành viên.
- **Dương Thiện Chí** - Thành viên.


---


## Giấy phép

Dự án được phát hành dưới [Giấy phép MIT](LICENSE). Vui lòng tham khảo tệp LICENSE để biết thêm chi tiết.


---


## Kế hoạch phát triển tương lai
- **Hỗ trợ lưu trữ đám mây**: Đồng bộ hóa dữ liệu với Google Drive hoặc Dropbox.
- **Cải tiến giao diện**: Sử dụng Material You để tùy chỉnh màu sắc giao diện theo người dùng.
- **Tích hợp AI**: Phân loại và nhận diện nội dung ảnh tự động.


---


### **Liên hệ**

- **Email**: mobilegroup5@gmail.com
- **Github**: [Mobile_Project_Group_05](https://github.com/IamBao2k4/Mobile_Project_Group_05)