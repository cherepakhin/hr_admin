<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Должности</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap" rel="stylesheet">
    <style> body { font-family: 'Inter', sans-serif; } </style>
</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Sidebar -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden p-6">
        <div class="max-w-4xl mx-auto bg-white shadow-xl rounded-lg overflow-hidden w-full">
            <div class="bg-sky-900 text-white p-6">
                <h1 class="text-2xl font-semibold">Управление должностями</h1>
            </div>
            <div class="p-6">
                <a href="/positions/new"
                   class="inline-block mb-4 px-4 py-2 bg-sky-900 text-white rounded hover:bg-sky-800">
                    Добавить должность
                </a>

                <table class="w-full text-left border-collapse">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="py-3 px-4 font-medium text-gray-800">Название</th>
                            <th class="py-3 px-4 font-medium text-gray-800 text-center">Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list positions as pos>
                        <tr class="border-b border-gray-100 hover:bg-gray-50">
                            <td class="py-3 px-4">${pos.name}</td>
                            <td class="py-3 px-4 text-center">
                                <a href="/positions/edit/${pos.id}"
                                   class="text-blue-600 hover:underline mr-4">Изменить</a>
                                <a href="/positions/delete/${pos.id}"
                                   onclick="return confirm('Удалить должность ${pos.name}?')"
                                   class="text-red-600 hover:underline">Удалить</a>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>