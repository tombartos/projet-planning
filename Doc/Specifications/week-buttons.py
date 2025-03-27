from itertools import count

WIDTH = 7.2574511
OFFSET = 105.00565 - 101.37128

for week, group, rect, text in zip(range(1, 53), count(start=63), count(start=60), count(start=61)):
    print(f"""    <g
       id="g{group}">
      <rect
         style="fill:#ffffff;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:0.449097;stroke-linecap:round;stroke-linejoin:round;stroke-dasharray:none;stroke-opacity:1"
         id="rect{rect}"
         width="{WIDTH}"
         height="4.4979167"
         x="{WIDTH * week}"
         y="53.364342"
         ry="2.2489583" />
      <text
         xml:space="preserve"
         style="font-size:2.82222px;line-height:1.25;font-family:sans-serif;-inkscape-font-specification:'sans-serif, Normal';text-align:center;letter-spacing:0px;word-spacing:0px;text-anchor:middle;stroke-width:0.264583"
         x="{WIDTH * week + OFFSET}"
         y="56.620834"
         id="text{text}"><tspan
           sodipodi:role="line"
           id="tspan62"
           style="font-size:2.82222px;text-align:center;text-anchor:middle;stroke-width:0.264583"
           x="{WIDTH * week + OFFSET}"
           y="56.620834">{week}</tspan></text>
    </g>""")
