@echo off
"C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HE:\\University\\Semester 5\\Mobile\\Project\\Mobile_Project_Group_05\\sdk\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=21" ^
  "-DANDROID_PLATFORM=android-21" ^
  "-DANDROID_ABI=x86_64" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86_64" ^
  "-DANDROID_NDK=C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\ndk\\26.1.10909125" ^
  "-DCMAKE_ANDROID_NDK=C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\ndk\\26.1.10909125" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\ndk\\26.1.10909125\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=E:\\University\\Semester 5\\Mobile\\Project\\Mobile_Project_Group_05\\sdk\\build\\intermediates\\cxx\\Debug\\2246z4h2\\obj\\x86_64" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=E:\\University\\Semester 5\\Mobile\\Project\\Mobile_Project_Group_05\\sdk\\build\\intermediates\\cxx\\Debug\\2246z4h2\\obj\\x86_64" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BE:\\University\\Semester 5\\Mobile\\Project\\Mobile_Project_Group_05\\sdk\\.cxx\\Debug\\2246z4h2\\x86_64" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"