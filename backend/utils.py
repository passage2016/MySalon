def get_string_weekday(int_weekday):
    week_list = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    return week_list[int_weekday]


def get_next_15min(in_day_time: str):
    time_list = in_day_time.split(":")
    int_time = int(time_list[0]) * 60 + int(time_list[1])
    int_time = int_time + 15
    new_time = "%d:%02d" % (int(int_time / 60), int_time % 60)
    return new_time


def time_before(time1: str, time2: str):
    iny_time1 = int(time1.split(":")[0]) * 60 + int(time1.split(":")[1])
    iny_time2 = int(time2.split(":")[0]) * 60 + int(time2.split(":")[1])
    if iny_time1 < iny_time2:
        return True
    return False
