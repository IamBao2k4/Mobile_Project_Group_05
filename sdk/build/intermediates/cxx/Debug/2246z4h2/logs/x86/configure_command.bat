@echo off
"D:\\Android\\SDK\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HD:\\HCMUS Slide\\Nam 3\\Phat trien phan mem di dong\\Mobile_Project_Group_05\\sdk\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=21" ^
  "-DANDROID_PLATFORM=android-21" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=D:\\Android\\SDK\\ndk\\26.1.10909125" ^
  "-DCMAKE_ANDROID_NDK=D:\\Android\\SDK\\ndk\\26.1.10909125" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\Android\\SDK\\ndk\\26.1.10909125\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\Android\\SDK\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=D:\\HCMUS Slide\\Nam 3\\Phat trien phan mem di dong\\Mobile_Project_Group_05\\sdk\\build\\intermediates\\cxx\\Debug\\2246z4h2\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=D:\\HCMUS Slide\\Nam 3\\Phat trien phan mem di dong\\Mobile_Project_Group_05\\sdk\\build\\intermediates\\cxx\\Debug\\2246z4h2\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BD:\\HCMUS Slide\\Nam 3\\Phat trien phan mem di dong\\Mobile_Project_Group_05\\sdk\\.cxx\\Debug\\2246z4h2\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
