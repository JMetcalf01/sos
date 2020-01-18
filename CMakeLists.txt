cmake_minimum_required(VERSION 3.15)
project(sos)

set(CMAKE_CXX_STANDARD 20)

add_executable(sos main.cpp Function.cpp Function.h variablestack.cpp variablestack.h)