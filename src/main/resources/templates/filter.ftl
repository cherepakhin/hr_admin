<!DOCTYPE html>
<html lang="en" x-data="{ sidebarOpen: true }" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Фильтр сотрудников</title>

    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Alpine.js for interactivity -->
    <script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body { font-family: 'Inter', sans-serif; }
        .btn-dark-blue { @apply bg-sky-900 hover:bg-sky-800 text-white; }
    </style>
</head>
<body class="bg-gray-50 min-h-screen flex">

    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Header -->
        <header class="bg-white shadow-sm p-6 border-b border-gray-100 py-3">
            <h2 class="text-2xl font-semibold text-gray-800">Фильтр сотрудников</h2>
        </header>

        <!-- Filter Form -->
        <main class="flex-1 p-6 flex items-start justify-center">
            <div class="bg-white shadow-xl border border-gray-100 w-full max-w-md p-6 space-y-6">

                <h3 class="text-xl font-medium text-gray-800 text-center">Найти сотрудников</h3>

                <form method="GET" action="/showEmployees" class="space-y-5">

                    <!-- First Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
                        <input type="text"
                               name="firstName"
                               placeholder="Например: Анна"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>

                    <!-- Last Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Фамилия</label>
                        <input type="text"
                               name="lastName"
                               placeholder="Например: Иванова"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>

                    <!-- Email -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                        <input type="text"
                               name="email"
                               placeholder="Например: anna@example.com"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>
                    <!-- Position Filter -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Должность</label>
                        <select name="positionId" class="w-full border border-gray-300 shadow-sm py-2 px-3"
                                onchange="this.form.submit()">
                            <option value="">Все</option>
                            <#list positions as pos>
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>
                    <!-- Buttons -->
                    <div class="flex gap-3 pt-2">
                        <button type="submit"
                                class="flex-1 bg-sky-900 text-white py-2 px-4 font-medium hover:bg-sky-800 transition">
                            Найти
                        </button>
                        <a href="/showEmployees"
                           class="flex-1 bg-gray-200 text-gray-700 py-2 px-4 font-medium text-center hover:bg-gray-300 transition">
                            Сбросить
                        </a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>