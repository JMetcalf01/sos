#ifndef SOS_NATIVEFUNCTION_H
#define SOS_NATIVEFUNCTION_H

#include <iostream>
#include "function.h"

namespace sos {
    class NativeFunction : public Function {
    private:
        void (*m_function) (VariableStack*);
    public:
        explicit NativeFunction(void (*function)(VariableStack*)) { m_function = function; }
        void execute(VariableStack *stack) override { m_function(stack); }

        static void print(VariableStack* stack);

        static void printr(VariableStack* stack);

        // !
        // Inverts a 1 or a 0 on the stack.
        static void invert(VariableStack* stack);

        // &&
        // If both elements on the stack are true (1)
        static void boolAnd(VariableStack* stack);

        // ||
        // If either element on the stack is true (1)
        static void boolOr(VariableStack* stack);

        // ==
        // If two elements on the stack are equal.
        static void equals(VariableStack* stack);

        // >
        // If the first element on the stack is greater than the second one.
        static void greater(VariableStack* stack);

        // >=
        // If the first element on the stack is greater than or equal to the second one.
        static void greaterEqual(VariableStack* stack);

        // <
        // If the first element on the stack is less than the second one.
        static void less(VariableStack* stack);

        // <=
        // If the first element on the stack is less than or equal to the second one.
        static void lessEqual(VariableStack* stack);
    };
}

#endif //SOS_NATIVEFUNCTION_H
