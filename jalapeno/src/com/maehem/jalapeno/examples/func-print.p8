-- Test basic function execution
-- Print a message from the _UPDATE() and _DRAW() functions.

x = 3

FUNCTION _INIT()
  PRINT("INIT")
END

FUNCTION _UPDATE()
  -- Update Stuff
  PRINT("The quick brown fox.")
END

FUNCTION _DRAW()
  PRINT("I like circles!")
END

function foobar( arg1, arg2 )
    x = 1 + arg1
end