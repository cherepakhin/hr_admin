<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Employee - HR Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .btn-dark-blue {
            @apply bg-sky-900 hover:bg-sky-800 text-white;
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Sidebar -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <div class="w-full max-w-6xl mx-0 bg-white shadow-xl border border-gray-100">
            <div class="bg-sky-900 px-6 py-4 text-white text-center">
                <h2 class="text-2xl font-semibold">Введите данные о новом сотруднике</h2>
            </div>
            <div class="p-6 overflow-x-auto">
                <form action="/employees" method="post" class="p-4 bg-white rounded-lg space-y-4">
                    <!-- First Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
                        <input
                            name="firstName"
                            class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500"
                            required
                            autofocus
                        />
                    </div>

                    <!-- Last Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Фамилия</label>
                        <input
                            name="lastName"
                            class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500"
                            required
                        />
                    </div>

                    <!-- Email -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                        <input
                            name="email"
                            type="email"
                            class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500"
                            required
                        />
                    </div>

                    <!-- Position -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Должность</label>
                        <select
                            name="position.id"
                            class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500 focus:outline-none"
                            required>
                            <#list positions as pos>
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>

                    <!-- Submit -->
                    <div class="flex gap-3 pt-2">
                        <button
                            type="submit"
                            class="btn-dark-blue w-full py-3 px-4 font-medium hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Сохранить
                        </button>
                        <a
                            href="/"
                            class="btn-dark-blue w-full py-3 px-4 font-medium text-center hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Назад
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>