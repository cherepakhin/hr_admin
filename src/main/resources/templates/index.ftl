<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">

    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Alpine.js for interactivity -->
    <script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

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
        .pagination-link {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-medium;
        }
        .pagination-current {
            font-weight: 900;
            color: cornflowerblue;
        }
        .logo-padding {
            padding-top: 0.9em;
            padding-bottom: 0.9em;
        }

        .all-employees {
            padding-top: 0.6em;
            padding-bottom: 0.6em;
            padding-left: 1.5em;
            padding-right: 1.5em;
        }
    </style>
</head>

<body class="bg-gray-50 min-h-screen flex" th:with="show_toolbar=true">
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Header -->
        <header class="bg-white shadow-sm border-b border-gray-100 all-employees">
            <div class="flex justify-between items-left">
                <h2 class="text-2xl font-semibold text-gray-800">Все сотрудники</h2>
            <div class="flex justify-between items-right">
                <a href="/employees/new"
                   class="btn-dark-blue px-5 py-2 font-medium shadow transition flex items-right gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 border-l-2 border-transparent hover:border-sky-900  w-40">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    Добавить
                </a>

                <h3 class="text-1xl py-2 px-4 font-semibold text-gray-800">Сортировать по:</h3>
                <!-- Combobox для сортировки с поддержкой выбранного значения -->
                <div class="relative">
                    <select
                        id="sortSelect"
                        onchange="window.location.href='?sortField=' + this.value + '&direction=asc'"
                        class="block w-40 px-5 py-2 bg-white border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500 appearance-none rounded-none">
                        <option value="firstName" <#if sortField?? && sortField == "firstName">selected</#if>>Имени</option>
                        <option value="lastName" <#if sortField?? && sortField == "lastName">selected</#if>>Фамилии</option>
                        <option value="email" <#if sortField?? && sortField == "email">selected</#if>>Email</option>
                        <option value="position.name" <#if sortField?? && sortField == "position.name">selected</#if>>Должности</option>
                    </select>
                    <!-- Кастомная стрелка -->
                    <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                        </svg>
                    </div>
                </div>
            </div>
        </header>

        <!-- Employee List -->
        <main class="flex-1 p-6 overflow-y-auto">
            <#if employees?has_content>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
                    <#list employees as employee>
                    <div class="bg-white p-6 shadow-md border border-gray-100 transition-all duration-200">
                        <div class="flex items-center mb-4">
                            <div class="w-12 h-12 bg-gradient-to-r from-sky-700 to-sky-900 flex items-center justify-center text-white font-bold text-lg">
                                ${employee.firstName[0]}
                            </div>
                            <div class="ml-4">
                                <h3 class="text-lg font-semibold text-gray-800">${employee.firstName} ${employee.lastName}</h3>
                                <p class="text-sm text-gray-500">${employee.position.name}</p>
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
                               onclick="return confirm('Вы уверены, что хотите удалить ${employee.firstName}?');"
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

                <!-- Pagination -->
                <div class="flex justify-between items-center mt-6">
                    <div class="text-sm text-gray-600">
                        Показано ${(currentPage * 10) + 1}–${(currentPage * 10) + employees?size} из ${totalElements}
                    </div>

                    <ul class="inline-flex space-x-1">
                        <!-- Previous Button -->
                        <#if currentPage gt 0>
                            <li>
                                <a href="/?page=${currentPage - 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">← Назад</a>
                            </li>
                        <#else>
                            <li><span class="pagination-link opacity-50 cursor-not-allowed">← Назад</span></li>
                        </#if>

                        <!-- Page Numbers -->
                        <#assign startPage = [currentPage - 2, 0]?max />
                        <#assign endPage = [(startPage + 4), totalPages - 1]?min />

                        <#list startPage..endPage as p>
                            <li>
                                <#if p == currentPage>
                                    <span class="pagination-current">${p + 1}</span>
                                <#else>
                                    <a href="/?page=${p}&sortField=${sortField}&direction=${direction}" class="pagination-link">${p + 1}</a>
                                </#if>
                            </li>
                        </#list>

                        <!-- Next Button -->
                        <#if currentPage lt totalPages - 1>
                            <li>
                                <a href="/?page=${currentPage + 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">Вперёд →</a>
                            </li>
                        <#else>
                            <li><span class="pagination-link opacity-50 cursor-not-allowed">Вперёд →</span></li>
                        </#if>
                    </ul>
                </div>
            <#else>
                <div class="bg-white p-10 shadow text-center border border-gray-100">
                    <svg xmlns="http://www.w3.org/2000/svg" class="mx-auto h-12 w-12 text-sky-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <h3 class="mt-4 text-lg font-medium text-gray-700">Сотрудники не найдены</h3>
                    <p class="text-gray-500 mt-1">Добавьте первого сотрудника.</p>
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