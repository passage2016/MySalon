package com.example.mysalon

fun mergeSort(arr:Array<Int>){
    var i = 2
    while(i/2 < arr.size){
        for(j in 0 until arr.size step i){
            var start1 = j
            var end1 = j + i/2
            if(end1 > arr.size){
                end1 = arr.size
            }
            var start2 = j + i/2
            if(start2 > arr.size){
                start2 = arr.size
            }
            var end2 = j + i
            if(end2 > arr.size){
                end2 = arr.size
            }

            var temp = Array<Int>(end2 - start1){0}
            var index = 0;
            while(start1 < end1 || start2 < end2){
                if(start1 < end1 && start2 < end2){
                    if(arr[start1] > arr[start2]){
                        temp[index++] = arr[start2++]
                    } else {
                        temp[index++] = arr[start1++]
                    }
                } else if(start1 < end1){
                    temp[index++] = arr[start1++]
                } else {
                    temp[index++] = arr[start2++]
                }
            }
            for(k in 0 until temp.size){
                arr[j + k] = temp[k]
            }
        }

        i = i * 2
    }
}

fun main(){
    var list = arrayOf(4, 4, 3, 6, 77, 2, 1, 5)
    mergeSort(list)
    println(list.asList())
}