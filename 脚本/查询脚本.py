import requests

# 设置接口地址
url = "http://localhost:8080/api/car"

# 车架编号列表
car_ids = [1, 2, 3]

# 遍历每个 carId，发送请求并打印结果
for car_id in car_ids:
    print(f"\n查询车辆 {car_id} 的警告信息:")
    
    # 构建请求参数
    params = {"carId": car_id}
    
    # 发送 GET 请求
    response = requests.get(url, params=params)
    
    # 检查响应状态
    if response.status_code == 200:
        data = response.json()
        if "data" in data:
            print(f"车辆 {car_id} 的警告信息：")
            print(data["data"])
        else:
            print(f"没有找到车辆 {car_id} 的警告信息。")
    else:
        print(f"请求失败，状态码：{response.status_code}")
