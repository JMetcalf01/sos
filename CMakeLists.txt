cmake_minimum_required(VERSION 3.15)
project(sos)

set(CMAKE_CXX_STANDARD 20)

add_executable(sos src/main.cpp src/function/function.h variablestack.cpp variablestack.h src/util.cpp src/util.h src/instructions.cpp src/instructions.h src/function/sourcefunction.cpp src/function/sourcefunction.h src/function/nativefunction.h)