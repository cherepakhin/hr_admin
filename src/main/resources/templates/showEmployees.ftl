<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>All Employees - HR Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>body{font-family:'Inter',sans-serif;}.btn-dark-blue{@apply bg-sky-900 hover:bg-sky-800 text-white;}</style>
</head>
<body class="bg-gray-50 min-h-screen flex">

    <!-- Include Sidebar Fragment -->
    <#include "/fragments/sidebar.ftl" />

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
    <div class="w-full max-w-6xl bg-white shadow-xl border border-gray-100">
        <div class="bg-sky-900 px-6 py-8 text-white text-center">
            <h2 class="text-2xl font-semibold">Список всех сотрудников</h2>
            <p class="text-sky-200">Все записи в базе данных</p>
        </div>
        <div class="p-6 overflow-x-auto">
            <#if employees?has_content>
                <table class="w-full text-sm text-left text-gray-700">
                    <thead class="uppercase border-t border-gray-100">
                        <tr>
                            <th class="px-6 py-3 font-medium">Имя</th>
                            <th class="px-6 py-3 font-medium">Фамилия</th>
                            <th class="px-6 py-3 font-medium">Email</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list employees as emp>
                        <tr class="border-b border-gray-100">
                            <td class="px-6 py-4">${emp.firstName}</td>
                            <td class="px-6 py-4">${emp.lastName}</td>
                            <td class="px-6 py-4">${emp.email}</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <p class="text-center py-6 text-gray-500">No employees found.</p>
            </#if>
        </div>
        <div class="bg-gray-50 px-6 py-4 border-t border-gray-100 text-center">
            <a href="/" class="btn-dark-blue inline-flex items-center px-4 py-2 font-medium hover:bg-sky-50 hover:text-sky-900">← Назад</a>
        </div>
    </div>
    </div>
</body>
</html>
