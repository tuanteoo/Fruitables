# Sử dụng image Nginx chính thức từ Docker Hub
FROM nginx:alpine

# Sao chép các file từ thư mục hiện tại vào thư mục html của Nginx
COPY ./frontend /usr/share/nginx/html

# Mặc định Nginx sẽ phục vụ nội dung trên cổng 80
EXPOSE 81
