# Gunakan Node.js sebagai base image
FROM node:16

# Set working directory
WORKDIR /app

# Salin package.json dan package-lock.json untuk instalasi dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Salin semua file proyek ke dalam container
COPY . .

# Expose port yang digunakan oleh aplikasi
EXPOSE 8080

# Jalankan aplikasi
CMD ["node", "app.js"]
