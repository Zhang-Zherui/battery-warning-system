import requests
import json
import random
import threading
import time  # 导入 time 模块用于控制延迟
from colorama import Fore, init
from datetime import datetime
# 初始化 colorama
init(autoreset=True)

# 接口地址
url = "http://localhost:8080/api/report"  # 所有线程发送到这个端口

# 发送请求的函数
def send_request(car_id, thread_num):
    for _ in range(10000000):
        # 随机生成 warnId
        warn_id = random.choice([1, 2])

        # 根据 carId 和 warnId 生成 signal 数据
        if car_id == 1:
            if warn_id == 1:
                # Mx 和 Mi 范围在 0.0-6.0 之间
                max_value = round(random.uniform(0.0, 6.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Mx": max_value, "Mi": min_value})
            elif warn_id == 2:
                # Ix 和 Ii 范围在 0.0-4.0 之间
                max_value = round(random.uniform(0.0, 4.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Ix": max_value, "Ii": min_value})
            else:
                # 没有 warnId，Mx、Mi、Ix、Ii 都有
                max_mx = round(random.uniform(0.0, 6.0), 2)
                delta_mx = round(random.uniform(0.0, max_mx), 2)
                min_mx = round(max_mx - delta_mx, 2)

                max_ix = round(random.uniform(0.0, 4.0), 2)
                delta_ix = round(random.uniform(0.0, max_ix), 2)
                min_ix = round(max_ix - delta_ix, 2)

                signal = json.dumps({
                    "Mx": max_mx, "Mi": min_mx,
                    "Ix": max_ix, "Ii": min_ix
                })
        elif car_id == 2:
            if warn_id == 1:
                max_value = round(random.uniform(0.0, 3.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Mx": max_value, "Mi": min_value})
            elif warn_id == 2:
                max_value = round(random.uniform(0.0, 2.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Ix": max_value, "Ii": min_value})
            else:
                max_mx = round(random.uniform(0.0, 3.0), 2)
                delta_mx = round(random.uniform(0.0, max_mx), 2)
                min_mx = round(max_mx - delta_mx, 2)

                max_ix = round(random.uniform(0.0, 2.0), 2)
                delta_ix = round(random.uniform(0.0, max_ix), 2)
                min_ix = round(max_ix - delta_ix, 2)

                signal = json.dumps({
                    "Mx": max_mx, "Mi": min_mx,
                    "Ix": max_ix, "Ii": min_ix
                })
        elif car_id == 3:
            if warn_id == 1:
                max_value = round(random.uniform(0.0, 6.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Mx": max_value, "Mi": min_value})
            elif warn_id == 2:
                max_value = round(random.uniform(0.0, 4.0), 2)
                delta = round(random.uniform(0.0, max_value), 2)
                min_value = round(max_value - delta, 2)
                signal = json.dumps({"Ix": max_value, "Ii": min_value})
            else:
                max_mx = round(random.uniform(0.0, 6.0), 2)
                delta_mx = round(random.uniform(0.0, max_mx), 2)
                min_mx = round(max_mx - delta_mx, 2)

                max_ix = round(random.uniform(0.0, 4.0), 2)
                delta_ix = round(random.uniform(0.0, max_ix), 2)
                min_ix = round(max_ix - delta_ix, 2)

                signal = json.dumps({
                    "Mx": max_mx, "Mi": min_mx,
                    "Ix": max_ix, "Ii": min_ix
                })

        # 构造请求体
        data = {
            "carId": car_id,
            "warnId": warn_id,
            "signal": signal
        }

        # 请求头
        headers = {
            "Content-Type": "application/json"
        }

        # 发送请求之前记录时间
        start_time = time.time()

        # 发送 POST 请求
        response = requests.post(url, data=json.dumps(data), headers=headers)

        # 计算从发送请求到接收响应的时间
        end_time = time.time()
        response_time = round(end_time - start_time, 4)  # 保留四位小数

        # 打印结果，使用不同颜色
        if thread_num == 1:
            print(f"[线程 1] 第{_+1}次请求 - 响应时间: {response_time}秒")
            if response.json().get("data") != None:
                data = response.json().get("data")
                print(f"车辆ID: {data.get('carId')} == 预警种类: {data.get('warnId')} == 告警级别: {data.get('alertLevel')} == 电池类型: {data.get('batteryType')}")
        elif thread_num == 2:
            print(Fore.GREEN + f"[线程 2] 第{_+1}次请求 - 响应时间: {response_time}秒")
            if response.json().get("data") != None:
                data = response.json().get("data")
                print(Fore.GREEN + f"车辆ID: {data.get('carId')} == 预警种类: {data.get('warnId')} == 告警级别: {data.get('alertLevel')} == 电池类型: {data.get('batteryType')}")
        elif thread_num == 3:
            print(Fore.YELLOW + f"[线程 3] 第{_+1}次请求 - 响应时间: {response_time}秒")
            if response.json().get("data") != None:
                data = response.json().get("data")
                print(Fore.YELLOW + f"车辆ID: {data.get('carId')} == 预警种类: {data.get('warnId')} == 告警级别: {data.get('alertLevel')} == 电池类型: {data.get('batteryType')}")


        time.sleep(0.1)

# 启动多个线程，每个线程处理不同的 carId
def start_threads():
    threads = []
    for i in range(3):
        thread = threading.Thread(target=send_request, args=(i+1, i+1))
        threads.append(thread)
        thread.start()

    # 等待所有线程结束
    for thread in threads:
        thread.join()

# 启动线程
start_threads()
