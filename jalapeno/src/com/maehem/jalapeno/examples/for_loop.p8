-- Moving Ball example
-- Aquired from:
--   https://lukemerrett.com/getting-started-with-pico-8/

X = 64  Y = 64

FUNCTION _INIT()
END


FUNCTION _UPDATE()
  IF ( BTN(0) ) THEN
    X=X-1
  END
END

FUNCTION _DRAW()
  PRINT( "Draw()" );
  -- RECTFILL(0,0,127,127,5)
  -- CIRCFILL(X,Y,7,8)
END

FUNCTION BTN( 0 )
  PRINT( "BTN() call")
  RETURN 1
END
