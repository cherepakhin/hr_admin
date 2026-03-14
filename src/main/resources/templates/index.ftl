<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .btn-dark-blue {
            @apply bg-sky-900 hover:bg-sky-800 text-white;
        }
        .sidebar-link:hover, .sidebar-link.active {
            @apply bg-gray-100 text-sky-900;
        }
        .sidebar-link svg {
            @apply transition-colors duration-200;
        }
        .sidebar-link:hover svg, .sidebar-link.active svg {
            @apply text-sky-900;
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen flex">

    <!-- Include Sidebar Fragment -->
    <#include "/fragments/sidebar.ftl" />

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Header -->
        <header class="bg-white shadow-sm p-6 border-b border-gray-100">
            <div class="flex justify-between items-center">
                <h2 class="text-2xl font-semibold text-gray-800">Все сотрудники</h2>
                <a href="/employees/new"
                   class="btn-dark-blue px-5 py-2 font-medium shadow transition flex items-center gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 border-l-2 border-transparent hover:border-sky-900">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    Добавить
                </a>
            </div>
        </header>

        <!-- Employee List -->
        <main class="flex-1 p-6 overflow-y-auto">
            <#if employees?has_content>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    <#list employees as employee>
                    <div class="bg-white p-6 shadow-md border border-gray-100 transition-all duration-200">
                        <div class="flex items-center mb-4">
                            <div class="w-12 h-12 bg-gradient-to-r from-sky-700 to-sky-900 flex items-center justify-center text-white font-bold text-lg">
                                ${employee.firstName[0]}
                            </div>
                            <div class="ml-4">
                                <h3 class="text-lg font-semibold text-gray-800">${employee.firstName} ${employee.lastName}</h3>
                                <p class="text-sm text-gray-500">${employee.email}</p>
                            </div>
                        </div>
                        <div class="flex justify-end gap-2">
                            <a href="/employees/edit/${employee.id}"
                               class="text-sky-900 hover:text-sky-700 font-medium text-sm transition flex items-center gap-1">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                </svg>
                                Изменить
                            </a>
                            <a href="/employees/delete/${employee.id}"
                               onclick="return confirm('Are you sure you want to delete ${employee.firstName}?');"
                               class="text-sky-900 hover:text-sky-700 font-medium text-sm transition flex items-center gap-1">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                </svg>
                                Удалить
                            </a>
                        </div>
                    </div>
                    </#list>
                </div>
            <#else>
                <div class="bg-white p-10 shadow text-center border border-gray-100">
                    <svg xmlns="http://www.w3.org/2000/svg" class="mx-auto h-12 w-12 text-sky-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <h3 class="mt-4 text-lg font-medium text-gray-700">No employees found</h3>
                    <p class="text-gray-500 mt-1">Get started by adding a new employee.</p>
                    <a href="/employees/new"
                       class="btn-dark-blue inline-flex items-center px-4 py-2 text-sm font-medium shadow transition mt-4">
                        Добавить сотрудника
                    </a>
                </div>
            </#if>
        </main>
    </div>
</body>
</html>