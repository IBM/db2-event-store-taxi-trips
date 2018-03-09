ifconfig | grep -A1 "virbr0" | grep "inet " | awk '{split($0,a," ");print a[2];}'
