FROM python:3-slim

WORKDIR /app

COPY . .

RUN pip install --no-cache-dir flask

EXPOSE 3000

CMD ["python", "api.py"]
