# MeoSea's Commands

Mod Fabric **chỉ chạy phía server** cho Minecraft, hiện có lệnh `/cwhitelist` để thêm tài khoản offline (crack) vào whitelist chỉ bằng tên, không cần tra cứu Mojang.

## Vì sao cần mod này

Lệnh `/whitelist add` của vanilla tra tên người chơi trên máy chủ Mojang. Tài khoản offline không tồn tại ở đó nên lệnh báo lỗi. `/cwhitelist` tự tính UUID offline từ tên rồi ghi thẳng vào whitelist của server.

## Yêu cầu

- Minecraft `26.3`
- Fabric Loader `0.19.5` trở lên
- Java `25` trở lên
- Fabric API

Chỉ cần cài trên server. Người chơi không cần cài mod này.

## Cài đặt

1. Tải file `meosea-commands-<phiên bản>.jar` (không phải file có đuôi `-sources`).
2. Bỏ vào thư mục `mods` của server, cạnh Fabric API.
3. Khởi động lại server.

## Lệnh

Cần quyền admin (op).

| Lệnh | Tác dụng |
| --- | --- |
| `/cwhitelist add <tên>` | Thêm tài khoản offline vào whitelist |
| `/cwhitelist remove <tên>` | Xóa tài khoản offline khỏi whitelist |
| `/cwhitelist list` | Liệt kê tên trong whitelist |

Tên hợp lệ gồm 3-16 ký tự: chữ cái, số và dấu gạch dưới. Whitelist cần được bật bằng `/whitelist on` (hoặc `white-list=true` trong `server.properties`).

## Lưu ý về UUID

UUID offline được tính theo công thức của vanilla từ chuỗi `OfflinePlayer:<tên>`, nên **phân biệt chữ hoa và chữ thường**: `Meo` và `meo` là hai UUID khác nhau. Người chơi phải đăng nhập bằng đúng tên đã được thêm.

Mod đăng nhập của server cũng cần cấp UUID offline chuẩn này. Mod chỉ ghi vào whitelist của server, không thay thế mod đăng nhập.

- `add` từ chối tên chỉ khác hoa/thường với tên đã có.
- `remove` chỉ xóa được mục offline. Mục của tài khoản premium dùng `/whitelist remove`.
- `list` hiện toàn bộ tên trong whitelist, kể cả mục không do mod này thêm.

## Build từ mã nguồn

```bash
git clone https://github.com/MeoSea/MeoSea-Commands.git
cd MeoSea-Commands
./gradlew build
```

Cần JDK 25. File mod nằm ở `build/libs/`. Thử nhanh bằng `./gradlew runServer`.

## Giấy phép

[MIT](LICENSE)
