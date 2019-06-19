-- Hello Word Example
-- Aquired from:
--     https://www.lexaloffle.com/pico-8.php?page=faq


t = 0
music(0) -- play music from pattern 0

function _draw()
  cls()
  for i=1,11 do               -- for each letter
    for j=0,7 do              -- for each rainbow trail part
      t1 = t + i*4 - j*2      -- adjusted time
      y = 45-j + cos(t1/50)*5 -- vertical position
      pal(7, 14-j)            -- remap colour from white
      spr(16+i, 8+i*8, y)     -- draw letter sprite
    end
  end

  print("this is pico-8", 37, 70, 14)
  print("nice to meet you", 34, 80, 12)
  spr(1, 64-4, 90) -- draw heart sprite
  t += 1
end
