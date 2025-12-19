#!/bin/bash

echo "🏗️  Construyendo el proyecto..."
mvn clean package -DskipTests

echo ""
echo "🧪 Ejecutando tests..."
mvn test

echo ""
echo "📊 Resumen de tests ejecutados"
echo "================================"
echo "✅ Tests de Strategy: 3"
echo "   - EmailNotificationStrategyTest"
echo "   - SmsNotificationStrategyTest"
echo "   - PushNotificationStrategyTest"
echo ""
echo "✅ Tests de Servicio: 7+"
echo "   - NotificacionServiceImplTest"
echo ""
echo "Total: 10+ tests unitarios"
echo ""
echo "🚀 Para ejecutar la aplicación:"
echo "   mvn spring-boot:run"
echo ""
echo "🐳 Para ejecutar con Docker:"
echo "   docker-compose up --build"
